package com.joshayoung.lazypizza.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joshayoung.lazypizza.search.presentation.searchItems.SearchItemsScreenRoot

@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Search
    ) {
        composable<Routes.Search> {
            SearchItemsScreenRoot()
        }
    }
}
