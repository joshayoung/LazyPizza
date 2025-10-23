package com.joshayoung.lazypizza.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.cart.presentation.CartScreenRoot
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItem
import com.joshayoung.lazypizza.history.presentation.HistoryScreenRoot
import com.joshayoung.lazypizza.menu.presentation.details.DetailsScreenRoot
import com.joshayoung.lazypizza.menu.presentation.home.HomeScreenRoot

@Composable
fun NavigationRoot(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        backStackEntry
            ?.destination
            ?.route
            ?.substringAfterLast(".")

    NavHost(
        navController = navController,
        startDestination = Routes.Menu
    ) {
        val bottomNavigationItems =
            listOf(
                BottomNavItem(
                    label = "Menu",
                    selected = currentRoute == "Menu",
                    clickAction = {
                        navController.navigate(Routes.Menu) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    },
                    imageResource = R.drawable.book
                ),
                BottomNavItem(
                    label = "Cart",
                    selected = currentRoute == "Cart",
                    clickAction = {
                        navController.navigate(Routes.Cart) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    },
                    imageResource = R.drawable.cart
                ),
                BottomNavItem(
                    label = "History",
                    selected = currentRoute == "History",
                    clickAction = {
                        navController.navigate(Routes.History) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    },
                    imageResource = R.drawable.history
                )
            )

        composable<Routes.Menu> {
            HomeScreenRoot(
                bottomNavItems = bottomNavigationItems,
                goToDetails = { id ->
                    navController.navigate(
                        Routes.Details.toString() + "?productId=$id"
                    )
                }
            )
        }

        composable(
            route = Routes.Details.toString() + "?productId={productId}",
            arguments =
                listOf(
                    navArgument(
                        name = "productId"
                    ) {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
        ) {
            DetailsScreenRoot(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Routes.Cart> {
            CartScreenRoot(
                bottomNavItems = bottomNavigationItems
            )
        }

        composable<Routes.History> {
            HistoryScreenRoot(
                bottomNavItems = bottomNavigationItems
            )
        }
    }
}
