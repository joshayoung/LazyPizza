package com.joshayoung.lazypizza.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.joshayoung.lazypizza.app.presentation.FirebaseAuthenticatorUiClient
import com.joshayoung.lazypizza.auth.ObserveAsEvents
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.utils.NavigationRoot
import com.joshayoung.lazypizza.core.utils.Routes
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { viewModel.state.isLoading }

        val firebaseAuthenticatorUiClient = FirebaseAuthenticatorUiClient(this)

        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            var isLoggedIn by remember { mutableStateOf(false) }
            isLoggedIn = firebaseAuthenticatorUiClient.isLoggedIn()

            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute =
                backStackEntry
                    ?.destination
                    ?.parent
                    ?.route
                    ?.substringAfterLast(".")

            ObserveAsEvents(firebaseAuthenticatorUiClient.authState) { authState ->
                isLoggedIn = authState.isLoggedIn

                if (isLoggedIn && currentRoute != Routes.Login.toString()) {
                    navController.navigate(Routes.Menu) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }

            LazyPizzaTheme {
                if (!viewModel.state.isLoading) {
                    NavigationRoot(
                        navController = navController,
                        cartItems = viewModel.state.cartItems,
                        firebaseAuthenticatorUiClient = firebaseAuthenticatorUiClient,
                        isLoggedIn = isLoggedIn
                    )
                }
            }
        }
    }
}
