package com.joshayoung.lazypizza.menu.presentation.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.primary8
import java.math.BigDecimal
import java.util.Locale

@Composable
fun AddButtonWithPrice(
    price: BigDecimal,
    onAction: () -> Unit
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val price = String.format(locale = Locale.US, "$%.2f", price)
        Text(price, style = MaterialTheme.typography.titleLarge)
        Button(
            onClick = {
                onAction()
            },
            contentPadding = PaddingValues(0.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
            modifier =
                Modifier
                    .border(
                        1.dp,
                        color = MaterialTheme.colorScheme.primary8,
                        shape = RoundedCornerShape(60.dp)
                    ).padding(horizontal = 20.dp)
                    .height(36.dp)
        ) {
            Text(
                "Add to Cart",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddButtonWithPricePreview() {
    LazyPizzaTheme {
        Box(modifier = Modifier.width(300.dp)) {
            AddButtonWithPrice(
                price = BigDecimal(2.00),
                onAction = {}
            )
        }
    }
}