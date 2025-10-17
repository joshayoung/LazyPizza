package com.joshayoung.lazypizza.core.presentation.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.ui.theme.CartIcon
import com.joshayoung.lazypizza.ui.theme.HistoryIcon
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.ui.theme.MenuIcon
import com.joshayoung.lazypizza.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.ui.theme.textPrimary

@Composable
fun LazyPizzaBottomBar(
    menuClick: () -> Unit,
    cartClick: () -> Unit,
    historyClick: () -> Unit
) {
    BottomAppBar(
        containerColor = Color.Transparent,
        actions = {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .dropShadow(
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                            shadow =
                                Shadow(
                                    radius = 16.dp,
                                    spread = 0.dp,
                                    color =
                                        MaterialTheme.colorScheme.textPrimary.copy(
                                            alpha = 0.6f
                                        ),
                                    offset = DpOffset(x = 0.dp, -(4).dp)
                                )
                        ).background(
                            MaterialTheme.colorScheme.surfaceHigher,
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .clickable {
                                menuClick()
                            }
                ) {
                    Icon(
                        MenuIcon,
                        contentDescription = null
                    )
                    Text("Menu")
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .clickable {
                                cartClick()
                            }
                ) {
                    Icon(
                        CartIcon,
                        contentDescription = null
                    )
                    Text("Cart")
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .clickable {
                                historyClick()
                            }
                ) {
                    Icon(
                        HistoryIcon,
                        contentDescription = null
                    )
                    Text("History")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LazyPizzaBottomBarPreview() {
    LazyPizzaTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyPizzaBottomBar(menuClick = {}, cartClick = {}, historyClick = {})
        }
    }
}