package com.example.swapcampus_djuissiange_fomigabriel.ui.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isep.composeapp.ui.AppContainer
import com.isep.shared.domain.entities.Category
import com.isep.shared.repository.ListingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Holds the state of the Listings screen.
 * It depends only on the ListingRepository interface, never on ListingCatalog.
 */
class ListingsViewModel(
    private val repository: ListingRepository = AppContainer.listingRepository
) : ViewModel() {

    // Private and modifiable inside the ViewModel, read-only for the screen.
    private val _uiState = MutableStateFlow<ListingsUiState>(ListingsUiState.Loading)
    val uiState: StateFlow<ListingsUiState> = _uiState.asStateFlow()

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> = _keyword.asStateFlow()

    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category.asStateFlow()

    init {
        refresh(showLoading = true)
    }

    fun onKeywordChange(value: String) {
        _keyword.value = value
        refresh()
    }

    fun onCategoryChange(value: Category?) {
        _category.value = value
        refresh()
    }

    fun refresh(showLoading: Boolean = false) {
        viewModelScope.launch {
            if (showLoading) {
                _uiState.value = ListingsUiState.Loading
                // The in-memory source answers instantly: this short pause
                // simulates a real data source so the spinner is visible.
                delay(400)
            }
            _uiState.value = try {
                val results = repository.searchListings(
                    keyword = _keyword.value.trim().ifBlank { null },
                    category = _category.value
                )
                ListingsUiState.Success(results)
            } catch (e: Exception) {
                ListingsUiState.Error(e.message ?: "Unable to load the listings")
            }
        }
    }
}
