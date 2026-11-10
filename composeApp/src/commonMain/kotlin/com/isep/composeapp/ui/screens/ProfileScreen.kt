package com.isep.composeapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isep.composeapp.resources.Res
import com.isep.composeapp.resources.action_login
import com.isep.composeapp.resources.action_logout
import com.isep.composeapp.resources.nav_profile
import com.isep.composeapp.resources.profile_email
import com.isep.composeapp.resources.profile_member_since
import com.isep.composeapp.resources.profile_name
import com.isep.composeapp.resources.profile_not_logged_in
import com.isep.composeapp.resources.profile_role
import com.isep.composeapp.ui.AppContainer
import com.isep.composeapp.ui.i18n.currentLanguageTag
import com.isep.composeapp.ui.i18n.formatDate
import org.jetbrains.compose.resources.stringResource

/**
 * Profile screen: account details, log out, language selector.
 * Common code: Android and iOS.
 */
@Composable
fun ProfileScreen(onLoginClick: () -> Unit) {
    val authRepository = AppContainer.authRepository
    val user by authRepository.currentUser.collectAsState()

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text(stringResource(Res.string.nav_profile), style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        val current = user
        if (current == null) {
            Text(stringResource(Res.string.profile_not_logged_in))
            Spacer(Modifier.height(8.dp))
            Button(onClick = onLoginClick) { Text(stringResource(Res.string.action_login)) }
        } else {
            Text(stringResource(Res.string.profile_name, current.name))
            Text(stringResource(Res.string.profile_email, current.email))
            Text(stringResource(Res.string.profile_role, current.role.toString()))
            Text(
                stringResource(
                    Res.string.profile_member_since,
                    formatDate(current.creationDate, currentLanguageTag())
                )
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = { authRepository.logout() }) { Text(stringResource(Res.string.action_logout)) }
        }

        Spacer(Modifier.height(24.dp))

        LanguageSelector()
    }
}