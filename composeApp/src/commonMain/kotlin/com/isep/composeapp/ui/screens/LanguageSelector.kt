package com.isep.composeapp.ui.screens

import androidx.compose.runtime.Composable

/**
 * Choix de la langue de l'app.
 * Android : boutons pour changer la langue de l'app.
 * iOS : explication, car la langue suit les Réglages du téléphone.
 */
@Composable
expect fun LanguageSelector()