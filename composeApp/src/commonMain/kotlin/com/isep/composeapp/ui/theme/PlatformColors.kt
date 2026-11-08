package com.isep.composeapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/** Color scheme for the current platform. Each platform provides its own version. */
@Composable
expect fun platformColorScheme(dark: Boolean): ColorScheme
