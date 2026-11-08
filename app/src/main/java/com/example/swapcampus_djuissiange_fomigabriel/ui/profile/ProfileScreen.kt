package com.example.swapcampus_djuissiange_fomigabriel.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isep.composeapp.ui.AppContainer

@Composable
fun ProfileScreen(onLoginClick: () -> Unit) {
    val authRepository = AppContainer.authRepository
    val user by authRepository.currentUser.collectAsState()

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Profil", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        val current = user
        if (current == null) {
            Text("Vous n'êtes pas connecté.")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onLoginClick) { Text("Se connecter") }
        } else {
            Text("Nom : ${current.name}")
            Text("Email : ${current.email}")
            Text("Rôle : ${current.role}")
            Spacer(Modifier.height(16.dp))
            Button(onClick = { authRepository.logout() }) { Text("Se déconnecter") }
        }
    }
}