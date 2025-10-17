package com.joshayoung.lazypizza.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.joshayoung.lazypizza.cart.CartScreenRoot
import com.joshayoung.lazypizza.history.HistoryScreenRoot
import com.joshayoung.lazypizza.menu.presentation.details.DetailsScreenRoot
import com.joshayoung.lazypizza.menu.presentation.home.HomeScreenRoot

@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Menu
    ) {
        composable<Routes.Menu> {
            HomeScreenRoot(
                goToDetails = { id ->
                    navController.navigate(
                        Routes.Details.toString() + "?productId=$id"
                    )
                },
                navigateToDetails = {
                    navController.navigate(Routes.Menu) {
                        popUpTo(Routes.Menu) {
                            inclusive = true
                        }
                    }
                },
                navigateToCart = {
                    navController.navigate(Routes.Cart) {
                        popUpTo(Routes.Cart) {
                            inclusive = true
                        }
                    }
                },
                navigateToHistory = {
                    navController.navigate(Routes.History) {
                        popUpTo(Routes.History) {
                            inclusive = true
                        }
                    }
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
                navigateToDetails = {
                    navController.navigate(Routes.Menu) {
                        popUpTo(Routes.Menu) {
                            inclusive = true
                        }
                    }
                },
                navigateToCart = {
                    navController.navigate(Routes.Cart) {
                        popUpTo(Routes.Cart) {
                            inclusive = true
                        }
                    }
                },
                navigateToHistory = {
                    navController.navigate(Routes.History) {
                        popUpTo(Routes.History) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Routes.History> {
            HistoryScreenRoot(
                navigateToDetails = {
                    navController.navigate(Routes.Menu) {
                        popUpTo(Routes.Menu) {
                            inclusive = true
                        }
                    }
                },
                navigateToCart = {
                    navController.navigate(Routes.Cart) {
                        popUpTo(Routes.Cart) {
                            inclusive = true
                        }
                    }
                },
                navigateToHistory = {
                    navController.navigate(Routes.History) {
                        popUpTo(Routes.History) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
