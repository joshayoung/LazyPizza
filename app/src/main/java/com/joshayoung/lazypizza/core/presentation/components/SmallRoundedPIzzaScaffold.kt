package com.joshayoung.lazypizza.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.core.ui.theme.BackIcon
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.core.ui.theme.surfaceHighest
import com.joshayoung.lazypizza.core.ui.theme.textPrimary

@Composable
fun SmallRoundedPizzaScaffold(
    backToCart: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.surfaceHighest)
                .padding(top = 60.dp)
                .dropShadow(
                    shape =
                        RoundedCornerShape(20.dp),
                    shadow =
                        Shadow(
                            radius = 16.dp,
                            spread = 0.dp,
                            color =
                                MaterialTheme.colorScheme.textPrimary.copy(alpha = 0.04f),
                            offset = DpOffset(x = 0.dp, -(4.dp))
                        )
                ).clip(RoundedCornerShape(20.dp)),
        topBar = {
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
    ) { innerPadding ->
        content(innerPadding)
    }
}
