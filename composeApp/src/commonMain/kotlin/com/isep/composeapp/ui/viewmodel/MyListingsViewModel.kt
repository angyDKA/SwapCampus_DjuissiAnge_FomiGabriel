package com.isep.composeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.isep.composeapp.ui.AppContainer
import com.isep.shared.domain.entities.Available
import com.isep.shared.domain.entities.Listing
import com.isep.shared.domain.entities.ListingStatus
import com.isep.shared.domain.entities.Reserved
import com.isep.shared.domain.entities.Sold
import com.isep.shared.domain.entities.UserAccount
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.isep.shared.repository.AuthRepository
import com.isep.shared.repository.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel of the "My Listings" screen: status changes and removal,
 * both reserved to the listing's owner. Common code: Android and iOS.
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


    fun reserve(listing: Listing, buyer: String) = changeStatus(listing, Reserved(buyer.trim()))

    // todayDate() comes from the shared domain (portable Android/iOS).
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

private fun today() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
