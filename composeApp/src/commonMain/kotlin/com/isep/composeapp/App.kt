package com.isep.composeapp

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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.isep.composeapp.resources.Res
import com.isep.composeapp.resources.nav_listings
import com.isep.composeapp.resources.nav_my_listings
import com.isep.composeapp.resources.nav_profile
import com.isep.composeapp.ui.AppContainer
import com.isep.composeapp.ui.navigation.EditRoute
import com.isep.composeapp.ui.navigation.ListingsRoute
import com.isep.composeapp.ui.navigation.LoginRoute
import com.isep.composeapp.ui.navigation.MyListingsRoute
import com.isep.composeapp.ui.navigation.PostRoute
import com.isep.composeapp.ui.navigation.ProfileRoute
import com.isep.composeapp.ui.screens.ListingFormScreen
import com.isep.composeapp.ui.screens.ListingsScreen
import com.isep.composeapp.ui.screens.LoginScreen
import com.isep.composeapp.ui.screens.MyListingsScreen
import com.isep.composeapp.ui.screens.ProfileScreen
import com.isep.composeapp.ui.theme.SwapCampusTheme
import com.isep.composeapp.ui.viewmodel.ListingsViewModel
import com.isep.shared.domain.entities.UserAccount
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/** One entry of the bottom navigation bar: a typed route, a translated label, an icon. */
private data class TopLevelDestination(
    val route: Any,
    val label: StringResource,
    val icon: ImageVector
)

private val destinations = listOf(
    TopLevelDestination(ListingsRoute, Res.string.nav_listings, Icons.Default.Home),
    TopLevelDestination(MyListingsRoute, Res.string.nav_my_listings, Icons.Default.ShoppingCart),
    TopLevelDestination(ProfileRoute, Res.string.nav_profile, Icons.Default.Person)
)

/**
 * Common entry point of the whole app, used by Android (MainActivity)
 * and iOS (MainViewController): theme, bottom bar and navigation.
 */
@Composable
fun App() {
    SwapCampusTheme {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntry?.destination
        val currentUser by AppContainer.authRepository.currentUser.collectAsStateWithLifecycle()


        val showBottomBar = destinations.any { currentDestination?.hasRoute(it.route::class) == true }


        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        destinations.forEach { destination ->
                            val label = stringResource(destination.label)
                            NavigationBarItem(
                                selected = currentDestination?.hasRoute(destination.route::class) == true,
                                onClick = {
                                    navController.navigate(destination.route) {
                                        popUpTo(ListingsRoute)
                                        launchSingleTop = true
                                    }
                                },
                                icon = { Icon(destination.icon, contentDescription = label) },
                                label = { Text(label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = ListingsRoute,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<ListingsRoute> {
                    val listingsViewModel: ListingsViewModel = viewModel { ListingsViewModel() }

                    LaunchedEffect(Unit) { listingsViewModel.refresh() }
                    ListingsScreen(viewModel = listingsViewModel)
                }

                composable<MyListingsRoute> {
                    MyListingsScreen(
                        onPostClick = { navController.navigate(PostRoute) },
                        onEditClick = { id -> navController.navigate(EditRoute(id)) }
                    )
                }

                composable<ProfileRoute> {
                    ProfileScreen(onLoginClick = { navController.navigate(LoginRoute) })
                }

                composable<LoginRoute> {
                    LoginScreen(onLoginSuccess = { navController.popBackStack() })
                }


                composable<PostRoute> {
                    RequireLogin(currentUser) {
                        ListingFormScreen(
                            listingId = null,
                            onDone = { navController.popBackStack() }
                        )
                    }
                }

                composable<EditRoute> { entry ->

                    val listingId = entry.toRoute<EditRoute>().id
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
}


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