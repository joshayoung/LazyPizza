package com.joshayoung.lazypizza.cart.presentation.confirmation.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshayoung.lazypizza.cart.presentation.checkout.CheckoutViewModel
import com.joshayoung.lazypizza.core.presentation.components.RoundedTopBar
import com.joshayoung.lazypizza.core.presentation.components.SmallRoundedPizzaScaffold
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfirmationScreenRoot(
    viewModel: CheckoutViewModel = koinViewModel(),
    backToMain: () -> Unit
) {
    ConfirmationScreen(
        backToMain = backToMain
    )
}

@Composable
fun ConfirmationScreen(backToMain: () -> Unit) {
    SmallRoundedPizzaScaffold(
        topBar = {
            RoundedTopBar(backToCart = backToMain)
        }
    ) { innerPadding ->
        val paddingVertical = 10.dp

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(14.dp)
        ) {
            Text(
                "Your order has been placed!",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(paddingVertical)
            )
            Text(
                "Thank you for your order! Please come at the indicated time.",
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .padding(paddingVertical)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier =
                    Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(10.dp)
                        ).padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                ) {
                    Text("Order Number:".uppercase())
                    Text("#1234", style = MaterialTheme.typography.titleSmall)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                ) {
                    Text("Pickup Time:".uppercase())
                    Text("September 25, 12:15", style = MaterialTheme.typography.titleSmall)
                }
            }

            TextButton(
                onClick = {
                    backToMain()
                },
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(
                    "Back to Menu",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmationScreenPreview() {
    LazyPizzaTheme {
        ConfirmationScreen(backToMain = {})
    }
}