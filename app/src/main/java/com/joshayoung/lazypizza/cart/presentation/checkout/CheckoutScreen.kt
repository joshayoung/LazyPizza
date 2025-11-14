package com.joshayoung.lazypizza.cart.presentation.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CheckoutScreenRoot() {
    CheckoutScreen()
}

@Composable
fun CheckoutScreen(
) {
    Column(modifier = Modifier.fillMaxSize()) {

    }
    Text(text = "Checkout")
}

@Preview(showBackground = true)
@Composable
private fun CheckoutScreenPreview() {
    CheckoutScreen()
}