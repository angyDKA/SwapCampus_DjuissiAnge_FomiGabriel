package com.isep.composeapp

import androidx.compose.ui.window.ComposeUIViewController

/** Called from Swift: wraps the common App() composable in a UIViewController. */
fun MainViewController() = ComposeUIViewController { App() }
