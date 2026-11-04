package com.example.swapcampus_djuissiange_fomigabriel.ui.post
import androidx.lifecycle.ViewModel
import com.isep.shared.domain.entities.Category
import com.isep.shared.domain.entities.UserAccount
import com.isep.shared.repository.AuthRepository
import com.isep.shared.repository.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostListingViewModel (
    private val listingRepository: ListingRepository,
    private val authRepository: AuthRepository
): ViewModel(){
    val currentUser: StateFlow<UserAccount?> = authRepository.currentUser
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _publisher = MutableStateFlow(false)
    val publisher: StateFlow<Boolean> = _publisher.asStateFlow()

    fun publish(
        title: String,
        description: String,
        priceText: String,
        category: Category?,
        photoUrl: String,
    ){
        val author = authRepository.currentUser.value
        if (author == null){
            _error.value = "Vous devez vous connecter pour publier"
            return
        }
        if (category == null){
            _error.value = "Veuillez choisir une categorie"
            return
        }
        val price = priceText.replace(',', ',').toDoubleOrNull()
        if (price == null){
            _error.value = "Veuillez entrer un nombre comme prix"
            return
        }
        runCatching {
            listingRepository.postListing(
                title = title.trim(),
                description = description.trim(),
                price = price,
                category = category,
                author = author,
                photoUrl = photoUrl.ifBlank { null }
            )
        }.onSuccess {
            _error.value = null
            _publisher.value = true
        }.onFailure {
           e -> _error.value = e.message
        }
    }
}