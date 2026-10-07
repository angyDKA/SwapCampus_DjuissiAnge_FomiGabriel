package com.example.swapcampus_djuissiange_fomigabriel.ui.listings

import com.isep.shared.domain.entities.Listing

/**
 * The three possible states of the Listings screen.
 * It is a sealed interface: a `when` on it needs no `else`.
 */
sealed interface ListingsUiState {
    data object Loading : ListingsUiState
    data class Success(val listings: List<Listing>) : ListingsUiState
    data class Error(val message: String) : ListingsUiState
}
