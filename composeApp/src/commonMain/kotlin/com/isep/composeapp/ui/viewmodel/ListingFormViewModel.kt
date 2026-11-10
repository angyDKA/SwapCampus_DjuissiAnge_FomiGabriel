package com.isep.composeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.isep.composeapp.resources.Res
import com.isep.composeapp.resources.error_invalid_listing
import com.isep.composeapp.resources.error_not_logged_in
import com.isep.composeapp.resources.error_not_owner
import com.isep.composeapp.resources.error_price_not_number
import com.isep.composeapp.ui.AppContainer
import com.isep.shared.domain.entities.Category
import com.isep.shared.domain.entities.Listing
import com.isep.shared.repository.AuthRepository
import com.isep.shared.repository.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel shared by the Post and Edit forms.
 * The business rules are NOT checked here: the repository checks them
 * and this ViewModel only forwards the message it receives.
 * Common code: Android and iOS.
 */
class ListingFormViewModel(
    private val listingRepository: ListingRepository = AppContainer.listingRepository,
    private val authRepository: AuthRepository = AppContainer.authRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<UiMessage?>(null)
    val errorMessage: StateFlow<UiMessage?> = _errorMessage.asStateFlow()

    fun findOwnListing(id: Int): Listing? {
        val user = authRepository.currentUser.value ?: return null
        return listingRepository.allListings()
            .firstOrNull { it.id == id && it.author.id == user.id }
    }

    fun save(
        listingId: Int?,
        title: String,
        description: String,
        priceText: String,
        category: Category,
        photoUrl: String
    ): Boolean {
        val user = authRepository.currentUser.value
        if (user == null) {
            _errorMessage.value = UiMessage.Resource(Res.string.error_not_logged_in)
            return false
        }
        if (listingId != null && findOwnListing(listingId) == null) {
            _errorMessage.value = UiMessage.Resource(Res.string.error_not_owner)
            return false
        }
        val price = priceText.replace(',', '.').toDoubleOrNull()
        if (price == null) {
            _errorMessage.value = UiMessage.Resource(Res.string.error_price_not_number)
            return false
        }
        val photo = photoUrl.trim().ifBlank { null }

        return try {
            if (listingId == null) {
                listingRepository.postListing(title, description, price, category, user, photo)
            } else {
                listingRepository.updateListing(listingId, title, description, price, category, photo)
            }
            _errorMessage.value = null
            true
        } catch (e: IllegalArgumentException) {
            _errorMessage.value = e.message?.let { UiMessage.Raw(it) }
                ?: UiMessage.Resource(Res.string.error_invalid_listing)
            false
        }
    }
}