package com.isep.shared.repository

import com.isep.shared.domain.UserRole
import com.isep.shared.domain.entities.UserAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate

class InMemoryAuthRepository: AuthRepository{
    private val accounts = listOf(
        UserAccount(
            id = 1,
            name = "Alice Martin",
            email = "alice@isep.fr",
            passwordHash = "alice123",
            role = UserRole.STUDENT,
            active = true,
            creationDate = LocalDate(2026, 9, 1)
        ),
        UserAccount(
            id = 2,
            name = "Bob Dupont",
            email = "bob@isep.fr",
            passwordHash = "bob123",
            role = UserRole.STUDENT,
            active = true,
            creationDate = LocalDate(2026, 9, 1)
        ),
        UserAccount(
            id = 3,
            name = "Admin SwapCampus",
            email = "admin@isep.fr",
            passwordHash = "admin123",
            role = UserRole.ADMINISTRATOR,
            active = true,
            creationDate = LocalDate(2026, 9, 1)
        )
    )
    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    override val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    override  fun authenticate(email: String, password: String): UserAccount? =
        accounts.find {
            it.email.equals(email.trim(), ignoreCase = true) &&
                    it.passwordHash == password && it.active
        }?.also { _currentUser.value = it }

    override fun logout() {
        _currentUser.value=null
    }
}