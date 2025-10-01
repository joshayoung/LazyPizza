package com.joshayoung.lazypizza.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(navController = navController,
        startDestination = Routes.UsernameScreen) {
        composable<Routes.UsernameScreen> {
        }
    }
}