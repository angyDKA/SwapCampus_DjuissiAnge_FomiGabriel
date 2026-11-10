package com.isep.composeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isep.composeapp.resources.Res
import com.isep.composeapp.resources.language_ios_hint
import com.isep.composeapp.resources.language_title
import org.jetbrains.compose.resources.stringResource

/** iOS : la langue d'une app suit les Réglages de l'iPhone, on l'explique simplement. */
@Composable
actual fun LanguageSelector() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(Res.string.language_title), style = MaterialTheme.typography.titleMedium)
        Text(stringResource(Res.string.language_ios_hint), style = MaterialTheme.typography.bodyMedium)
    }
}