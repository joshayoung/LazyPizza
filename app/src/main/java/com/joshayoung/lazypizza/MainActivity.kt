package com.joshayoung.lazypizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.initialize
import com.joshayoung.lazypizza.navigation.NavigationRoot
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(context = this)
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user == null) {
            auth.signInAnonymously()
        }

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition { viewModel.state.isLoading }

        enableEdgeToEdge()
        setContent {
            LazyPizzaTheme {
                val navController = rememberNavController()
                NavigationRoot(navController = navController)
            }
        }
    }
}
