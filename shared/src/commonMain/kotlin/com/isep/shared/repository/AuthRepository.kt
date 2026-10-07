package com.isep.shared.repository

import com.isep.shared.domain.entities.UserAccount
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<UserAccount?>
    fun authenticate(email: String, password: String): UserAccount?
    fun logout()
}