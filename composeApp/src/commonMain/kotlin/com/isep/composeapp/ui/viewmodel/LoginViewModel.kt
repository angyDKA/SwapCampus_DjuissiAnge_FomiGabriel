package com.isep.composeapp.ui.viewmodel
import androidx.lifecycle.ViewModel
import com.isep.shared.domain.entities.UserAccount
import com.isep.shared.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Why the login failed. The screen turns it into a text in the current language. */
enum class LoginError { EMPTY_FIELDS, INVALID_CREDENTIALS }

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _error = MutableStateFlow<LoginError?>(null)
    val error: StateFlow<LoginError?> = _error.asStateFlow()

    val currentUser: StateFlow<UserAccount?> = authRepository.currentUser

    fun login(email: String, password: String) {
        _error.value = when {
            email.isBlank() || password.isBlank() -> LoginError.EMPTY_FIELDS
            authRepository.authenticate(email, password) == null -> LoginError.INVALID_CREDENTIALS
            else -> null
        }
    }

    fun logout() = authRepository.logout()
}
