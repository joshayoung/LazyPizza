package com.joshayoung.lazypizza.search.presentation.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun QuantitySelector(itemCount: MutableState<Int>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier =
        Modifier
    ) {
        CountButton("-", itemCount, {
            if (itemCount.value > 0) {
                itemCount.value -= 1
            }
        })
        Text(
            text = itemCount.value.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier =
                Modifier
                    .padding(horizontal = 20.dp)
        )
        CountButton("+", itemCount, { itemCount.value += 1 })
    }
}

@Composable
fun CountButton(
    text: String,
    itemCount: MutableState<Int>,
    action: () -> Unit
) {
    Button(
        contentPadding = PaddingValues(0.dp),
        modifier =
            Modifier
                .border(1.dp, Color.LightGray, shape = RoundedCornerShape(4.dp))
                .size(20.dp),
        onClick = {
            action()
        },
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
    ) {
        Text(
            color = MaterialTheme.colorScheme.onSurface,
            text = text,
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuantitySelectorPreview() {
    LazyPizzaTheme {
        val itemCount = remember { mutableIntStateOf(2) }
        QuantitySelector(
            itemCount = itemCount
        )
    }
}
