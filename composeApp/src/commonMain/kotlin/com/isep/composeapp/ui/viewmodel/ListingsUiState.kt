package com.isep.composeapp.ui.viewmodel

import com.isep.shared.domain.entities.Listing

sealed interface ListingsUiState {
    data object Loading : ListingsUiState
    data class Success(val listings: List<Listing>) : ListingsUiState
    data class Error(val message: String) : ListingsUiState
}
