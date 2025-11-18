package com.joshayoung.lazypizza.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.core.ui.theme.BackIcon
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher

@Composable
fun RoundedTopBar(backToCart: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.surfaceHigher)
                .padding(top = 20.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
    ) {
        Box(
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.5f))
                    .size(30.dp)
        ) {
            IconButton(onClick = {
                backToCart()
            }) {
                Icon(
                    imageVector = BackIcon,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    contentDescription = null
                )
            }
        }
        Spacer(Modifier.weight(1f))
        Text(
            "Order Checkout",
            modifier = Modifier,
            style =
                MaterialTheme.typography.titleSmall
        )
        Spacer(Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
private fun RoundedTopAppBarPreview() {
    RoundedTopBar(backToCart = {})
}