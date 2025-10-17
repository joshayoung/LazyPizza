package com.joshayoung.lazypizza.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.joshayoung.lazypizza.cart.CartScreenRoot
import com.joshayoung.lazypizza.history.HistoryScreenRoot
import com.joshayoung.lazypizza.search.presentation.details.DetailsScreenRoot
import com.joshayoung.lazypizza.search.presentation.home.HomeScreenRoot

@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Search
    ) {
        composable<Routes.Search> {
            HomeScreenRoot(
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
                navigateToDetails = {
                    navController.navigate(Routes.Search)
                },
                navigateToCart = {
                    navController.navigate(Routes.Cart)
                },
                navigateToHistory = {
                    navController.navigate(Routes.History)
                }
            )
        }

        composable<Routes.Cart> {
            CartScreenRoot()
        }

        composable<Routes.History> {
            HistoryScreenRoot()
        }
    }
}
