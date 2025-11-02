package com.joshayoung.lazypizza.auth.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme

@Composable
fun LoginScreenRoot() {
    LoginScreen()
}

@Composable
fun LoginScreen() {
    Text("Login")
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LazyPizzaTheme {
        LoginScreen()
    }
}
