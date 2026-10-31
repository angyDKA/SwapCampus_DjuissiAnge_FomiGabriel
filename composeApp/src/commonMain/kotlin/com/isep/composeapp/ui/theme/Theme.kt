package com.isep.composeapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/** Material 3 theme of the app: follows the system dark mode; the colors depend on the platform. */
@Composable
fun SwapCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = platformColorScheme(dark = isSystemInDarkTheme()),
        content = content
    )
}
