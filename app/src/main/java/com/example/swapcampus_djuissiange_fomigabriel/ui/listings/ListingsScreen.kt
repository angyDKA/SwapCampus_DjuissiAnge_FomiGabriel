package com.example.swapcampus_djuissiange_fomigabriel.ui.listings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isep.composeapp.ui.viewmodel.ListingsUiState
import com.isep.composeapp.ui.viewmodel.ListingsViewModel
import com.isep.shared.domain.describePhoto
import com.isep.shared.domain.entities.Category
import com.isep.shared.domain.entities.Listing
import com.isep.shared.domain.entities.label
import com.isep.composeapp.ui.i18n.currentLanguageTag
import com.isep.composeapp.ui.i18n.formatDate
import com.isep.composeapp.ui.i18n.formatPrice

/**
 * Listings screen (exercise 4): search field, category filter and list of cards.
 * Accessible without authentication.
 */
@Composable
fun ListingsScreen(
    modifier: Modifier = Modifier,
    viewModel: ListingsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyword by viewModel.keyword.collectAsStateWithLifecycle()
    val category by viewModel.category.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = keyword,
            onValueChange = viewModel::onKeywordChange,
            label = { Text("Search by keyword") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        CategoryFilter(selected = category, onSelected = viewModel::onCategoryChange)
        Spacer(Modifier.height(8.dp))

        // The three UiState branches are handled explicitly, with no else.
        when (val state = uiState) {
            is ListingsUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is ListingsUiState.Error -> Text(
                text = state.message,
                color = MaterialTheme.colorScheme.error
            )

            is ListingsUiState.Success ->
                if (state.listings.isEmpty()) {
                    Text("No listing matches your search.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.listings, key = { it.id }) { listing ->
                            ListingCard(listing)
                        }
                    }
                }
        }
    }
}

@Composable
private fun CategoryFilter(
    selected: Category?,
    onSelected: (Category?) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            FilterChip(
                selected = selected == null,
                onClick = { onSelected(null) },
                label = { Text("All") }
            )
        }
        items(Category.entries) { category ->
            FilterChip(
                selected = selected == category,
                onClick = { onSelected(category) },
                label = { Text(category.label()) }
            )
        }
    }
}

@Composable
private fun ListingCard(listing: Listing) {
    val languageTag = currentLanguageTag()
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image placeholder (the photos are not downloaded yet).
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = listing.describePhoto()
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(listing.title, style = MaterialTheme.typography.titleMedium)
                Text(formatPrice(listing.price, languageTag), style = MaterialTheme.typography.bodyMedium)
                Text(formatDate(listing.creationDate, languageTag), style = MaterialTheme.typography.bodySmall)
                Text(listing.category.label(), style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
