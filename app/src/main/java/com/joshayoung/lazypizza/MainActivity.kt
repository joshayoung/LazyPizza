package com.joshayoung.lazypizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            true
        }

        enableEdgeToEdge()
        setContent {
            LazyPizzaTheme {
                StartPage()
            }
        }
    }
}

@Composable
fun StartPage() {
    LaunchedEffect(Unit) {
        delay(3000)
    }
    Text(text = "Lazy Pizza")
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LazyPizzaTheme {
    }
}