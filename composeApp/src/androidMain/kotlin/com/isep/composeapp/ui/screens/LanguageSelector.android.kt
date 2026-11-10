package com.isep.composeapp.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.isep.composeapp.resources.Res
import com.isep.composeapp.resources.language_system
import com.isep.composeapp.resources.language_title
import org.jetbrains.compose.resources.stringResource

/** Android : l'app peut choisir sa propre langue, indépendamment de celle du téléphone. */
@Composable
actual fun LanguageSelector() {
    // Langue choisie dans l'app ; vide = celle du téléphone.
    val current = AppCompatDelegate.getApplicationLocales().toLanguageTags()
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(Res.string.language_title), style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LanguageChip(stringResource(Res.string.language_system), current.isEmpty()) { setAppLanguage("") }
            LanguageChip("English", current.startsWith("en")) { setAppLanguage("en") }
            LanguageChip("Français", current.startsWith("fr")) { setAppLanguage("fr") }
        }
    }
}

@Composable
private fun LanguageChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(label) })
}

/** Change la langue de l'app ; Android recrée l'écran dans la nouvelle langue. */
private fun setAppLanguage(tag: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
}