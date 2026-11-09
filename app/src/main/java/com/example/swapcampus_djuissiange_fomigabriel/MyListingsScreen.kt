package com.example.swapcampus_djuissiange_fomigabriel

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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isep.composeapp.ui.AppContainer
import com.isep.shared.domain.entities.Available
import com.isep.shared.domain.entities.Listing
import com.isep.shared.domain.entities.ListingStatus
import com.isep.shared.domain.entities.Reserved
import com.isep.shared.domain.entities.Sold
import com.isep.shared.domain.entities.UserAccount
import com.isep.shared.domain.entities.label
import com.isep.shared.repository.AuthRepository
import com.isep.shared.repository.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.isep.composeapp.ui.i18n.currentLanguageTag
import com.isep.composeapp.ui.i18n.formatPrice

/**
 * ViewModel of the "My Listings" screen: status changes (exercise 7)
 * and removal (exercise 8), both reserved to the listing's owner.
 */
class MyListingsViewModel(
    private val listingRepository: ListingRepository = AppContainer.listingRepository,
    private val authRepository: AuthRepository = AppContainer.authRepository
) : ViewModel() {

    val currentUser: StateFlow<UserAccount?> = authRepository.currentUser

    private val _myListings = MutableStateFlow<List<Listing>>(emptyList())
    val myListings: StateFlow<List<Listing>> = _myListings.asStateFlow()

    fun refresh() {
        val user = authRepository.currentUser.value
        _myListings.value =
            if (user == null) emptyList()
            else listingRepository.allListings().filter { it.author.id == user.id }
    }

    // Transitions from the specifications:
    // Available -> Reserved(buyer), Reserved -> Sold(saleDate), Reserved/Sold -> Available
    fun reserve(listing: Listing, buyer: String) = changeStatus(listing, Reserved(buyer.trim()))

    fun markAsSold(listing: Listing) = changeStatus(listing, Sold(today()))

    fun cancel(listing: Listing) = changeStatus(listing, Available)

    fun remove(listing: Listing) {
        if (!isOwner(listing)) return
        listingRepository.removeListing(listing.id)
        refresh()
    }

    private fun changeStatus(listing: Listing, newStatus: ListingStatus) {
        if (!isOwner(listing)) return
        listingRepository.changeStatus(listing.id, newStatus)
        refresh()
    }

    private fun isOwner(listing: Listing): Boolean =
        authRepository.currentUser.value?.id == listing.author.id
}

private fun today(): LocalDate =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

@Composable
fun MyListingsScreen(
    onPostClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    viewModel: MyListingsViewModel = viewModel()
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
        Text("My Listings", style = MaterialTheme.typography.headlineSmall)

        if (currentUser == null) {
            Text("Log in from the Profile tab to post and manage your listings.")
        } else {
            Button(onClick = onPostClick, modifier = Modifier.fillMaxWidth()) {
                Text("Post a listing")
            }
            if (myListings.isEmpty()) {
                Text("You have not posted any listing yet.")
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

    // Exercise 8: confirmation before the removal actually happens.
    listingToRemove?.let { listing ->
        AlertDialog(
            onDismissRequest = { listingToRemove = null },
            title = { Text("Remove this listing?") },
            text = { Text("${listing.title} will be permanently removed.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.remove(listing)
                        listingToRemove = null
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { listingToRemove = null }) {
                    Text("Keep")
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
            Text("${formatPrice(listing.price, currentLanguageTag())} - ${listing.category.label()}")
            Text(listing.status.label(), style = MaterialTheme.typography.labelMedium)

            // Exhaustive when on the sealed class, with no else.
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                when (listing.status) {
                    is Available -> TextButton(onClick = onReserve) { Text("Reserve") }

                    is Reserved -> {
                        TextButton(onClick = onMarkAsSold) { Text("Mark as sold") }
                        TextButton(onClick = onCancel) { Text("Cancel") }
                    }

                    is Sold -> TextButton(onClick = onCancel) { Text("Cancel") }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onEdit) { Text("Edit") }
                TextButton(onClick = onRemove) { Text("Remove") }
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
        title = { Text("Reserve this listing") },
        text = {
            OutlinedTextField(
                value = buyer,
                onValueChange = { buyer = it },
                label = { Text("Buyer name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(buyer) },
                enabled = buyer.isNotBlank()
            ) {
                Text("Reserve")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
