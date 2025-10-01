package com.joshayoung.lazypizza

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.compose.rememberNavController
import com.joshayoung.lazypizza.navigation.NavigationRoot
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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