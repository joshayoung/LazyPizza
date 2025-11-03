package com.joshayoung.lazypizza.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.joshayoung.lazypizza.auth.presentation.LoginScreenRoot
import com.joshayoung.lazypizza.cart.presentation.cart_list.CartScreenRoot
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItemUi
import com.joshayoung.lazypizza.core.ui.theme.CartIcon
import com.joshayoung.lazypizza.core.ui.theme.HistoryIcon
import com.joshayoung.lazypizza.core.ui.theme.MenuIcon
import com.joshayoung.lazypizza.history.presentation.order_history.HistoryScreenRoot
import com.joshayoung.lazypizza.menu.presentation.details.DetailsScreenRoot
import com.joshayoung.lazypizza.menu.presentation.home.HomeScreenRoot

@Composable
fun NavigationRoot(navController: NavHostController, cartItems: Int) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        backStackEntry
            ?.destination
            ?.route
            ?.substringAfterLast(".")

    val bottomNavigationItems =
        listOf(
            BottomNavItemUi(
                label = "Menu",
                selected = currentRoute == "Menu",
                clickAction = {
                    navController.navigate(Routes.Menu) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                imageVector = MenuIcon
            ),
            BottomNavItemUi(
                label = "Cart",
                selected = currentRoute == "Cart",
                clickAction = {
                    navController.navigate(Routes.Cart) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                imageVector = CartIcon
            ),
            BottomNavItemUi(
                label = "History",
                selected = currentRoute == "History",
                clickAction = {
                    navController.navigate(Routes.History) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                imageVector = HistoryIcon
            )
        )

    NavHost(
        navController = navController,
        startDestination = Routes.Menu
    ) {
        composable<Routes.Menu> {
            HomeScreenRoot(
                bottomNavItemUis = bottomNavigationItems,
                goToLoginScreen = {
                    navController.navigate(Routes.Login)
                },
                cartItems = cartItems,
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
                },
                navigateToCart = {
                    navController.navigate(Routes.Cart) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Routes.Cart> {
            CartScreenRoot(
                bottomNavItemUis = bottomNavigationItems,
                cartItems = cartItems,
                backToMenu = {
                    navController.navigate(Routes.Menu) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Routes.History> {
            HistoryScreenRoot(
                cartItems = cartItems,
                bottomNavItemUis = bottomNavigationItems,
                goToLogin = {
                    navController.navigate(Routes.Login) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Routes.Login> {
            LoginScreenRoot(
                useAsGuest = {
                    navController.navigate(Routes.Menu) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
