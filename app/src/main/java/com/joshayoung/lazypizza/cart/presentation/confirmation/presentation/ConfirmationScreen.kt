package com.joshayoung.lazypizza.cart.presentation.confirmation.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.lazypizza.core.utils.Routes

@Composable
fun ConfirmationScreenRoot() {
    ConfirmationScreen()
}

@Composable
fun ConfirmationScreen() {
    Text(text = "Confirmation")
}

@Preview
@Composable
private fun ConfirmationScreenPreview() {
    ConfirmationScreen()
}