package com.joshayoung.lazypizza.menu.presentation.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme

@Composable
fun ProductHeader(
    itemCount: Int,
    name: String,
    onAction: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
    ) {
        Text(name, fontWeight = FontWeight.Bold)
        if (itemCount > 0) {
            IconButton(
                onClick = {
                    onAction()
                },
                modifier =
                    Modifier
                        .border(
                            1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(8.dp)
                        ).padding(6.dp)
                        .size(20.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.trash),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier =
                    Modifier
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductHeaderPreview() {
    LazyPizzaTheme {
        ProductHeader(
            name = "test",
            onAction = {},
            itemCount = 1
        )
    }
}
