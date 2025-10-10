package com.joshayoung.lazypizza.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.joshayoung.lazypizza.search.presentation.details.DetailsScreenRoot
import com.joshayoung.lazypizza.search.presentation.searchItems.SearchItemsScreenRoot

@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Search
    ) {
        composable<Routes.Search> {
            SearchItemsScreenRoot(
                goToDetails = { product ->
                    navController.navigate(
                        Routes.Details.toString() + "?pizza=$product"
                    )
                }
            )
        }

        composable(
            route = Routes.Details.toString() + "?pizza={pizza}",
            arguments =
                listOf(
                    navArgument(
                        name = "pizza"
                    ) {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
        ) {
            DetailsScreenRoot()
        }
    }
}
