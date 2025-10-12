package com.joshayoung.lazypizza.search.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme

@Composable
fun PriceAndAddButton(
    price: String,
    itemCount: MutableState<Int>
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(price, style = MaterialTheme.typography.titleLarge)
        Button(
            onClick = {
                itemCount.value += 1
            },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
        ) {
            Text(
                "Add to Cart",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriceAndButtonPreview() {
    LazyPizzaTheme {
        val itemCount = remember { mutableIntStateOf(1) }
        Box(modifier = Modifier.width(300.dp)) {
            PriceAndAddButton("1.20", itemCount)
        }
    }
}