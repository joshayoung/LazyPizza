package com.joshayoung.lazypizza.core.presentation.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaColors
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme

@Composable
fun QuantitySelectorTwo(
    itemCount: Int,
    increment: () -> Unit,
    decrement: () -> Unit,
    inCart: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier =
        Modifier
    ) {
        if (inCart) {
            DecrementComponentTWo(
                itemCount = itemCount,
                decrement = decrement
            )
        } else {
            CountButtonTwo(
                "-",
                onAction = { decrement() }
            )
        }
        Text(
            text = itemCount.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier =
                Modifier
                    .padding(horizontal = 20.dp)
        )
        CountButtonTwo("+", onAction = { increment() })
    }
}

@Composable
fun DecrementComponentTWo(
    itemCount: Int,
    decrement: () -> Unit,
) {
    if (itemCount < 2) {
        CountButtonTwo(
            "-",
            { },
            modifier =
                Modifier
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(4.dp)
                    ),
            textColor = LazyPizzaColors.textSecondary
        )
    } else {
        CountButtonTwo(
            "-",
            onAction = { decrement() }
        )
    }
}

@Composable
fun CountButtonTwo(
    text: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Button(
        contentPadding = PaddingValues(0.dp),
        modifier =
            modifier
                .border(1.dp, Color.LightGray, shape = RoundedCornerShape(4.dp))
                .size(20.dp),
        onClick = {
            onAction()
        },
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
    ) {
        Text(
            color = textColor,
            text = text,
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuantitySelectorTwoPreview() {
    LazyPizzaTheme {
        QuantitySelector(
            itemCount = 1,
            increment = {},
            decrement = {},
            updateCart = {},
            inCart = true
        )
    }
}
