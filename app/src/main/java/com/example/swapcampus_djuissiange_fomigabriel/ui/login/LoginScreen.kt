package com.example.swapcampus_djuissiange_fomigabriel.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swapcampus_djuissiange_fomigabriel.AppContainer

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {
    val viewModel: LoginViewModel = viewModel { LoginViewModel(AppContainer.authRepository) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val error by viewModel.error.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    LaunchedEffect(user) { if (user != null) onLoginSuccess() }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("SwapCampus – Connexion", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Mot de passe") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Se connecter") }

        error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        user?.let {
            Spacer(Modifier.height(8.dp))
            Text("Connecté : ${it.name}")
            TextButton(onClick = { viewModel.logout() }) { Text("Se déconnecter") }
        }
    }
}