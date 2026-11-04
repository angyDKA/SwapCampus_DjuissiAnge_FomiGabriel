package com.example.swapcampus_djuissiange_fomigabriel.ui.mylistings

import androidx.lifecycle.ViewModel
import com.isep.shared.domain.entities.Listing
import com.isep.shared.repository.AuthRepository
import com.isep.shared.repository.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyListingsViewModel(
    private val listingRepository: ListingRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _myListings = MutableStateFlow<List<Listing>>(emptyList())
    val myListings: StateFlow<List<Listing>> = _myListings.asStateFlow()

    /** Recharge uniquement les annonces de l'utilisateur connecté. */
    fun refresh() {
        val user = authRepository.currentUser.value
        _myListings.value =
            if (user == null) emptyList()
            else listingRepository.allListings().filter { it.author.id == user.id }
    }

    /** Suppression définitive, seulement si l'utilisateur est le propriétaire. */
    fun remove(listing: Listing) {
        val user = authRepository.currentUser.value ?: return
        if (listing.author.id != user.id) return      // sécurité : pas le propriétaire → on ne fait rien
        listingRepository.removeListing(listing.id)
        refresh()                                      // met la liste à jour à l'écran
    }
}