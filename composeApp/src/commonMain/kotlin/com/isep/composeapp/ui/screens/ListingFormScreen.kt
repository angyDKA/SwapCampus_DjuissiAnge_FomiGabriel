package com.isep.composeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isep.composeapp.resources.Res
import com.isep.composeapp.resources.action_cancel
import com.isep.composeapp.resources.action_post_listing
import com.isep.composeapp.resources.action_save
import com.isep.composeapp.resources.form_field_category
import com.isep.composeapp.resources.form_field_description
import com.isep.composeapp.resources.form_field_photo_url
import com.isep.composeapp.resources.form_field_price
import com.isep.composeapp.resources.form_field_title
import com.isep.composeapp.resources.form_not_found
import com.isep.composeapp.resources.form_title_edit
import com.isep.composeapp.ui.i18n.text
import com.isep.composeapp.ui.viewmodel.ListingFormViewModel
import com.isep.composeapp.ui.viewmodel.UiMessage
import com.isep.composeapp.ui.viewmodel.text
import com.isep.shared.domain.entities.Category
import com.isep.shared.domain.entities.Listing
import org.jetbrains.compose.resources.stringResource

/**
 * Form screen. listingId == null: post a new listing.
 * Otherwise: edit, pre-filled from the existing listing (owner only).
 * Common code: Android and iOS.
 */
@Composable
fun ListingFormScreen(
    listingId: Int?,
    onDone: () -> Unit,
    viewModel: ListingFormViewModel = viewModel { ListingFormViewModel() }
) {
    val existing = remember(listingId) { listingId?.let { viewModel.findOwnListing(it) } }
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    if (listingId != null && existing == null) {
        Text(
            text = stringResource(Res.string.form_not_found),
            modifier = Modifier.padding(16.dp)
        )
    } else {
        ListingForm(
            existing = existing,
            errorMessage = errorMessage,
            onSave = { title, description, priceText, category, photoUrl ->
                val saved = viewModel.save(listingId, title, description, priceText, category, photoUrl)
                if (saved) onDone()
            },
            onCancel = onDone
        )
    }
}

@Composable
private fun ListingForm(
    existing: Listing?,
    errorMessage: UiMessage?,
    onSave: (String, String, String, Category, String) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(existing?.title ?: "") }
    var description by rememberSaveable { mutableStateOf(existing?.description ?: "") }
    var priceText by rememberSaveable { mutableStateOf(existing?.price?.toString() ?: "") }
    var category by rememberSaveable { mutableStateOf(existing?.category ?: Category.OTHER) }
    var photoUrl by rememberSaveable { mutableStateOf(existing?.photoUrl ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(
                if (existing == null) Res.string.action_post_listing else Res.string.form_title_edit
            ),
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(Res.string.form_field_title)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(Res.string.form_field_description)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = priceText,
            onValueChange = { priceText = it },
            label = { Text(stringResource(Res.string.form_field_price)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Text(stringResource(Res.string.form_field_category), style = MaterialTheme.typography.labelLarge)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Category.entries) { item ->
                FilterChip(
                    selected = category == item,
                    onClick = { category = item },
                    label = { Text(item.text()) }
                )
            }
        }
        OutlinedTextField(
            value = photoUrl,
            onValueChange = { photoUrl = it },
            label = { Text(stringResource(Res.string.form_field_photo_url)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        errorMessage?.let { message ->
            Text(message.text(), color = MaterialTheme.colorScheme.error)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onSave(title, description, priceText, category, photoUrl) }) {
                Text(stringResource(Res.string.action_save))
            }
            TextButton(onClick = onCancel) {
                Text(stringResource(Res.string.action_cancel))
            }
        }
    }
}