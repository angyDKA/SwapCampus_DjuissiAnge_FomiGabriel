package com.example.swapcampus_djuissiange_fomigabriel.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.isep.composeapp.ui.AppContainer
import com.example.swapcampus_djuissiange_fomigabriel.ui.listings.ListingsScreen
import com.isep.composeapp.ui.screens.LoginScreen
import com.example.swapcampus_djuissiange_fomigabriel.ui.mylistings.MyListingsScreen
import com.example.swapcampus_djuissiange_fomigabriel.ui.post.PostListingScreen
import com.example.swapcampus_djuissiange_fomigabriel.ui.profile.ProfileScreen

// Un onglet de la barre du bas : sa route, son libellé, son icône
private data class BottomTab(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    BottomTab(Routes.LISTINGS, "Annonces", Icons.Default.Home),
    BottomTab(Routes.MY_LISTINGS, "Mes annonces", Icons.Default.ShoppingCart),
    BottomTab(Routes.PROFILE, "Profil", Icons.Default.Person)
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = { BottomBar(navController, currentRoute) },
        floatingActionButton = {
            // Bouton "+" visible seulement sur la liste des annonces
            if (currentRoute == Routes.LISTINGS) {
                FloatingActionButton(onClick = { navController.navigate(Routes.POST) }) {
                    Icon(Icons.Default.Add, contentDescription = "Publier une annonce")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.LISTINGS,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LISTINGS) {
                ListingsScreen()
            }
            composable(Routes.MY_LISTINGS) {
                RequireLogin(navController) {
                    MyListingsScreen()
                }
            }
            composable(Routes.PROFILE) {
                ProfileScreen(onLoginClick = { navController.navigate(Routes.LOGIN) })
            }
            composable(Routes.LOGIN) {
                // Après connexion : retour à l'écran d'où l'on venait
                LoginScreen(onLoginSuccess = { navController.popBackStack() })
            }
            composable(Routes.POST) {
                RequireLogin(navController) {
                    PostListingScreen(onPublisher = { navController.popBackStack() })
                }
            }
        }
    }
}

/** Route protégée : affiche [content] si connecté, sinon redirige vers le Login. */
@Composable
private fun RequireLogin(navController: NavHostController, content: @Composable () -> Unit) {
    val user by AppContainer.authRepository.currentUser.collectAsState()
    if (user != null) {
        content()
    } else {
        LaunchedEffect(Unit) { navController.navigate(Routes.LOGIN) }
    }
}

@Composable
private fun BottomBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar {
        tabs.forEach { tab ->
            NavigationBarItem(
                selected = currentRoute == tab.route,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(Routes.LISTINGS)   // évite d'empiler les écrans à l'infini
                        launchSingleTop = true     // pas deux fois le même écran
                    }
                },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) }
            )
        }
    }
}