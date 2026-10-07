package com.isep.shared.repository

object AppContainer {
    val authRepository: AuthRepository = InMemoryAuthRepository()
}