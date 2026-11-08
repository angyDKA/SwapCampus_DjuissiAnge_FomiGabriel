package com.example.swapcampus_djuissiange_fomigabriel.ui.post

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isep.composeapp.ui.AppContainer
import com.isep.shared.domain.entities.Category
import com.isep.shared.domain.entities.label

@Composable
fun PostListingScreen(onPublisher: () -> Unit = {}){
    val viewModel: PostListingViewModel = viewModel() {
        PostListingViewModel(AppContainer.listingRepository, AppContainer.authRepository)
    }
    val user by viewModel.currentUser.collectAsState()
    val error by viewModel.error.collectAsState()
    val publisher by viewModel.publisher.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf("") }
    var category by remember { mutableStateOf<Category?>(null) }

    LaunchedEffect(publisher) { if (publisher) onPublisher() }

    //security
    if (user == null){
        Box(Modifier.fillMaxSize().padding(24.dp)){
            Text("Vous devez vous connecter pour publier une annonce.")
        }
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        OutlinedTextField(
            value = title, onValueChange = { title=it},
            label = { Text("Titre")}, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = description, onValueChange = { description=it},
            label = {Text("Description")}, minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = priceText, onValueChange = {priceText=it},
            label = {Text("Prix")}, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = photoUrl, onValueChange = {photoUrl=it},
            label = {Text("URL de la photo (facultatif)")}, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        Text("Categorie", style = MaterialTheme.typography.labelLarge)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Category.entries){
                c-> FilterChip(
                    selected = category == c,
                    onClick = { category = c},
                    label = {Text(c.label())}
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {viewModel.publish(title, description, priceText, category, photoUrl)},
            modifier = Modifier.fillMaxWidth()
        ) { Text("Publier") }

        error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        if (publisher){
            Spacer(Modifier.height(8.dp))
            Text ("L'annonce a ete publier !", color = MaterialTheme.colorScheme.primary)
        }
    }
}