package com.example.swapcampus_djuissiange_fomigabriel

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.swapcampus_djuissiange_fomigabriel.ui.listings.ListingsScreen
import com.isep.composeapp.ui.viewmodel.ListingsViewModel
import com.isep.composeapp.ui.AppContainer
import com.isep.composeapp.ui.screens.LoginScreen
import com.isep.shared.domain.entities.UserAccount

/** One entry of the bottom navigation bar. */
private data class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private const val ROUTE_LISTINGS = "listings"
private const val ROUTE_MY_LISTINGS = "my_listings"
private const val ROUTE_PROFILE = "profile"
private const val ROUTE_POST = "post"
private const val ARG_LISTING_ID = "listingId"
private const val ROUTE_EDIT = "edit/{listingId}"

private val destinations = listOf(
    TopLevelDestination(ROUTE_LISTINGS, "Listings", Icons.Default.Home),
    TopLevelDestination(ROUTE_MY_LISTINGS, "My Listings", Icons.Default.ShoppingCart),
    TopLevelDestination(ROUTE_PROFILE, "Profile", Icons.Default.Person)
)

/**
 * Root of the UI (exercise 9): a NavHost linking all the screens,
 * plus a simple navigation bar (Listings / My Listings / Profile).
 */
@Composable
fun SwapCampusApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentUser by AppContainer.authRepository.currentUser.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            NavigationBar {
                destinations.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(ROUTE_LISTINGS)
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_LISTINGS,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Accessible without authentication.
            composable(ROUTE_LISTINGS) {
                val listingsViewModel: ListingsViewModel = viewModel()
                // Reload the list each time we come back to this screen.
                LaunchedEffect(Unit) { listingsViewModel.refresh() }
                ListingsScreen(viewModel = listingsViewModel)
            }

            composable(ROUTE_MY_LISTINGS) {
                MyListingsScreen(
                    onPostClick = { navController.navigate(ROUTE_POST) },
                    onEditClick = { id -> navController.navigate("edit/$id") }
                )
            }

            // Login / logout screen written for exercise 2.
            composable(ROUTE_PROFILE) { LoginScreen() }

            // Routes protected by authentication.
            composable(ROUTE_POST) {
                RequireLogin(currentUser) {
                    ListingFormScreen(
                        listingId = null,
                        onDone = { navController.popBackStack() }
                    )
                }
            }

            composable(
                route = ROUTE_EDIT,
                arguments = listOf(navArgument(ARG_LISTING_ID) { type = NavType.IntType })
            ) { entry ->
                val listingId = entry.arguments?.getInt(ARG_LISTING_ID) ?: return@composable
                RequireLogin(currentUser) {
                    ListingFormScreen(
                        listingId = listingId,
                        onDone = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

/** Shows the login form instead of the content when nobody is logged in. */
@Composable
private fun RequireLogin(
    currentUser: UserAccount?,
    content: @Composable () -> Unit
) {
    if (currentUser == null) {
        LoginScreen()
    } else {
        content()
    }
}
