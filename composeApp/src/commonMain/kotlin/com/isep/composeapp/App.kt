package com.isep.composeapp

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isep.composeapp.ui.AppContainer
import com.isep.composeapp.ui.screens.LoginScreen
import com.isep.composeapp.ui.screens.WelcomeScreen
import com.isep.composeapp.ui.theme.SwapCampusTheme
import com.isep.composeapp.ui.viewmodel.LoginViewModel

/**
 * Common entry point used by iOS (and usable by Android later).
 * Login screen while nobody is logged in, welcome screen afterwards.
 */
@Composable
fun App() {
    SwapCampusTheme {
        Surface(
            modifier = Modifier.safeDrawingPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            val viewModel: LoginViewModel = viewModel { LoginViewModel(AppContainer.authRepository) }
            val user by viewModel.currentUser.collectAsStateWithLifecycle()

            val account = user
            if (account == null) {
                // The state (currentUser) changes on login: the screen switches by itself.
                LoginScreen()
            } else {
                WelcomeScreen(name = account.name, onLogout = viewModel::logout)
            }
        }
    }
}
