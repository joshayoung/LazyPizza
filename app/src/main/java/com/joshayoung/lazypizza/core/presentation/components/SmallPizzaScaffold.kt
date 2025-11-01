package com.joshayoung.lazypizza.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme

@Composable
fun SmallPizzaScaffold(
    topAppBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topAppBar,
        bottomBar = bottomBar
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
@Preview
fun LazyPizzaScaffoldPreview() {
    LazyPizzaTheme {
        SmallPizzaScaffold(
            topAppBar = { PizzaAppBar() },
            bottomBar = {
                PizzaBottomBar(
                    bottomNavItemUis = emptyList()
                )
            }
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(Color.Gray),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Content",
                    color = Color.Blue
                )
            }
        }
    }
}