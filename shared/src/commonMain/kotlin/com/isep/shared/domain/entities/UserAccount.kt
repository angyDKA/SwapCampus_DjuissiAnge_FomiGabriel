package com.isep.shared.domain.entities

import com.isep.shared.domain.UserRole
import kotlinx.datetime.LocalDate

data class UserAccount(
    val id: Int,
    val name: String,
    val email: String,
    val passwordHash: String,
    val creationDate: LocalDate,
    val role: UserRole,
    val active: Boolean = true
)
