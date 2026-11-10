package com.isep.composeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isep.composeapp.resources.Res
import com.isep.composeapp.resources.action_login
import com.isep.composeapp.resources.login_demo_accounts
import com.isep.composeapp.resources.login_email
import com.isep.composeapp.resources.login_logged_in_as
import com.isep.composeapp.resources.login_password
import com.isep.composeapp.resources.login_title
import com.isep.composeapp.resources.action_logout
import com.isep.composeapp.ui.AppContainer
import com.isep.composeapp.ui.i18n.text
import com.isep.composeapp.ui.viewmodel.LoginViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {
    val viewModel: LoginViewModel = viewModel { LoginViewModel(AppContainer.authRepository) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val error by viewModel.error.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    LaunchedEffect(user) { if (user != null) onLoginSuccess() }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(Res.string.login_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text(stringResource(Res.string.login_email)) }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text(stringResource(Res.string.login_password)) }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) { Text(stringResource(Res.string.action_login)) }

        error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it.text(), color = MaterialTheme.colorScheme.error)
        }
        user?.let {
            Spacer(Modifier.height(8.dp))
            Text(stringResource(Res.string.login_logged_in_as, it.name))
            TextButton(onClick = { viewModel.logout() }) { Text(stringResource(Res.string.action_logout)) }
        }
        Spacer(Modifier.height(16.dp))
        Text(stringResource(Res.string.login_demo_accounts), style = MaterialTheme.typography.bodySmall)
    }
}
