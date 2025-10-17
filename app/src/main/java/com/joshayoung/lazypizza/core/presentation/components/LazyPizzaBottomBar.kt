package com.joshayoung.lazypizza.core.presentation.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.lazypizza.ui.theme.CartIcon
import com.joshayoung.lazypizza.ui.theme.HistoryIcon
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.ui.theme.MenuIcon

@Composable
fun LazyPizzaBottomBar(
    menuClick: () -> Unit,
    cartClick: () -> Unit,
    historyClick: () -> Unit
) {
    BottomAppBar(
        actions = {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier =
                    Modifier
                        .fillMaxWidth()
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

@Preview
@Composable
fun LazyPizzaBottomBarPreview() {
    LazyPizzaTheme {
        LazyPizzaBottomBar(menuClick = {}, cartClick = {}, historyClick = {})
    }
}