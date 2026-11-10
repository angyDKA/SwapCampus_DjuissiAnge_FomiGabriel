package com.isep.composeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isep.composeapp.resources.Res
import com.isep.composeapp.resources.action_cancel
import com.isep.composeapp.resources.action_edit
import com.isep.composeapp.resources.action_keep
import com.isep.composeapp.resources.action_mark_sold
import com.isep.composeapp.resources.action_post_listing
import com.isep.composeapp.resources.action_remove
import com.isep.composeapp.resources.action_reserve
import com.isep.composeapp.resources.my_listings_empty
import com.isep.composeapp.resources.my_listings_login_required
import com.isep.composeapp.resources.nav_my_listings
import com.isep.composeapp.resources.remove_dialog_text
import com.isep.composeapp.resources.remove_dialog_title
import com.isep.composeapp.resources.reserve_buyer_name
import com.isep.composeapp.resources.reserve_dialog_title
import com.isep.composeapp.ui.i18n.currentLanguageTag
import com.isep.composeapp.ui.i18n.formatPrice
import com.isep.composeapp.ui.i18n.text
import com.isep.composeapp.ui.viewmodel.MyListingsViewModel
import com.isep.shared.domain.entities.Available
import com.isep.shared.domain.entities.Listing
import com.isep.shared.domain.entities.Reserved
import com.isep.shared.domain.entities.Sold
import org.jetbrains.compose.resources.stringResource

/**
 * "My Listings" screen: the logged-in user's listings, with status changes,
 * edit and removal (with confirmation). Common code: Android and iOS.
 */
@Composable
fun MyListingsScreen(
    onPostClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    viewModel: MyListingsViewModel = viewModel { MyListingsViewModel() }
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val myListings by viewModel.myListings.collectAsStateWithLifecycle()

    // Reload each time the screen is displayed or the logged-in user changes.
    LaunchedEffect(currentUser) { viewModel.refresh() }

    var listingToRemove by remember { mutableStateOf<Listing?>(null) }
    var listingToReserve by remember { mutableStateOf<Listing?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(Res.string.nav_my_listings), style = MaterialTheme.typography.headlineSmall)

        if (currentUser == null) {
            Text(stringResource(Res.string.my_listings_login_required))
        } else {
            Button(onClick = onPostClick, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(Res.string.action_post_listing))
            }
            if (myListings.isEmpty()) {
                Text(stringResource(Res.string.my_listings_empty))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(myListings, key = { it.id }) { listing ->
                        MyListingCard(
                            listing = listing,
                            onEdit = { onEditClick(listing.id) },
                            onReserve = { listingToReserve = listing },
                            onMarkAsSold = { viewModel.markAsSold(listing) },
                            onCancel = { viewModel.cancel(listing) },
                            onRemove = { listingToRemove = listing }
                        )
                    }
                }
            }
        }
    }


    listingToRemove?.let { listing ->
        AlertDialog(
            onDismissRequest = { listingToRemove = null },
            title = { Text(stringResource(Res.string.remove_dialog_title)) },
            text = { Text(stringResource(Res.string.remove_dialog_text, listing.title)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.remove(listing)
                        listingToRemove = null
                    }
                ) {
                    Text(stringResource(Res.string.action_remove))
                }
            },
            dismissButton = {
                TextButton(onClick = { listingToRemove = null }) {
                    Text(stringResource(Res.string.action_keep))
                }
            }
        )
    }

    listingToReserve?.let { listing ->
        ReserveDialog(
            onConfirm = { buyer ->
                viewModel.reserve(listing, buyer)
                listingToReserve = null
            },
            onDismiss = { listingToReserve = null }
        )
    }
}

@Composable
private fun MyListingCard(
    listing: Listing,
    onEdit: () -> Unit,
    onReserve: () -> Unit,
    onMarkAsSold: () -> Unit,
    onCancel: () -> Unit,
    onRemove: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(listing.title, style = MaterialTheme.typography.titleMedium)
            Text("${formatPrice(listing.price, currentLanguageTag())} - ${listing.category.text()}")
            Text(listing.status.text(), style = MaterialTheme.typography.labelMedium)

            // Exhaustive when on the sealed class, with no else.
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                when (listing.status) {
                    is Available -> TextButton(onClick = onReserve) {
                        Text(stringResource(Res.string.action_reserve))
                    }

                    is Reserved -> {
                        TextButton(onClick = onMarkAsSold) { Text(stringResource(Res.string.action_mark_sold)) }
                        TextButton(onClick = onCancel) { Text(stringResource(Res.string.action_cancel)) }
                    }

                    is Sold -> TextButton(onClick = onCancel) { Text(stringResource(Res.string.action_cancel)) }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onEdit) { Text(stringResource(Res.string.action_edit)) }
                TextButton(onClick = onRemove) { Text(stringResource(Res.string.action_remove)) }
            }
        }
    }
}

@Composable
private fun ReserveDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var buyer by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.reserve_dialog_title)) },
        text = {
            OutlinedTextField(
                value = buyer,
                onValueChange = { buyer = it },
                label = { Text(stringResource(Res.string.reserve_buyer_name)) },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(buyer) },
                enabled = buyer.isNotBlank()
            ) {
                Text(stringResource(Res.string.action_reserve))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.action_cancel))
            }
        }
    )
}