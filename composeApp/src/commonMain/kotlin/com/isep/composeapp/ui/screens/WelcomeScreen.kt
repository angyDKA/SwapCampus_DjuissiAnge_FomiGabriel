package com.isep.composeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isep.composeapp.platform
import com.isep.composeapp.resources.Res
import com.isep.composeapp.resources.welcome_logout
import com.isep.composeapp.resources.welcome_platform
import com.isep.composeapp.resources.welcome_title
import org.jetbrains.compose.resources.stringResource

/** Shown after a successful login. Common code: the same screen on Android and iOS. */
@Composable
fun WelcomeScreen(name: String, onLogout: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(Res.string.welcome_title, name),
            style = MaterialTheme.typography.headlineMedium
        )
        // platform() is the expect/actual function from Platform.kt: "Android" or "iOS".
        Text(
            stringResource(Res.string.welcome_platform, platform()),
            style = MaterialTheme.typography.bodyLarge
        )
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.welcome_logout))
        }
    }
}
