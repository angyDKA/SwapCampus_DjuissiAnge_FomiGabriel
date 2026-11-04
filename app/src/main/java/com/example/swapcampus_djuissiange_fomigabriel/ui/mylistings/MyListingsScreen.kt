package com.example.swapcampus_djuissiange_fomigabriel.ui.mylistings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swapcampus_djuissiange_fomigabriel.AppContainer
import com.isep.shared.domain.entities.Listing
import com.isep.shared.domain.entities.label

@Composable
fun MyListingsScreen() {
    val viewModel: MyListingsViewModel = viewModel {
        MyListingsViewModel(AppContainer.listingRepository, AppContainer.authRepository)
    }
    val listings by viewModel.myListings.collectAsState()

    // L'annonce dont on demande la suppression (null = pas de boîte de dialogue)
    var listingToDelete by remember { mutableStateOf<Listing?>(null) }

    // Recharge la liste à chaque arrivée sur l'écran
    LaunchedEffect(Unit) { viewModel.refresh() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mes annonces", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        if (listings.isEmpty()) {
            Text("Vous n'avez encore publié aucune annonce.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listings, key = { it.id }) { listing ->
                    MyListingCard(
                        listing = listing,
                        onDeleteClick = { listingToDelete = listing }   // ouvre la confirmation
                    )
                }
            }
        }
    }

    // Boîte de dialogue de confirmation (exo 8)
    listingToDelete?.let { listing ->
        AlertDialog(
            onDismissRequest = { listingToDelete = null },   // clic à côté → annule
            title = { Text("Supprimer l'annonce ?") },
            text = { Text("« ${listing.title} » sera supprimée définitivement.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.remove(listing)
                    listingToDelete = null
                }) { Text("Supprimer", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { listingToDelete = null }) { Text("Annuler") }
            }
        )
    }
}

@Composable
private fun MyListingCard(listing: Listing, onDeleteClick: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {          // prend toute la place restante
                Text(listing.title, style = MaterialTheme.typography.titleMedium)
                Text("${listing.price} €")
                Text(listing.category.label(), style = MaterialTheme.typography.labelMedium)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}