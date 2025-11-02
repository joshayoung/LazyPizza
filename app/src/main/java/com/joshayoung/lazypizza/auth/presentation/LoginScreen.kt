package com.joshayoung.lazypizza.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    useAsGuest: () -> Unit
) {
    LoginScreen(
        state = viewModel.state,
        useAsGuest = useAsGuest
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    useAsGuest: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 200.dp)
                .padding(horizontal = 10.dp)
    ) {
        Text(
            text = "Welcome to LazyPizza",
            style = MaterialTheme.typography.titleLarge,
            modifier =
                Modifier
                    .padding(bottom = 6.dp)
        )
        Text(
            text = "Enter your phone number",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        BasicTextField(
            state = TextFieldState(),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(14.dp)
        )

        Button(
            onClick = {},
            enabled = false,
            modifier =
                Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()
        ) {
            Text(text = "Continue")
        }
        Text(
            modifier =
                Modifier
                    .clickable {
                        useAsGuest()
                    },
            text = "Continue without signing in",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LazyPizzaTheme {
        LoginScreen(
            useAsGuest = {},
            state = LoginState()
        )
    }
}
