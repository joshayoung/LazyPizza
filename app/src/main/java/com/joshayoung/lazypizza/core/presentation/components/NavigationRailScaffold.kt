package com.joshayoung.lazypizza.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.core.presentation.utils.previewBottomNavItems
import com.joshayoung.lazypizza.core.utils.BottomNavItem
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.ui.theme.surfaceHigher

@Composable
fun NavigationRailScaffold(
    title: String? = null,
    appBarItems: List<BottomNavItem>,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
        ) {
            title?.let { pageTitle ->
                Text(
                    pageTitle,
                    style = MaterialTheme.typography.titleLarge,
                    modifier =
                        Modifier
                            .align(Alignment.TopCenter)
                )
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
            ) {
                Box {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surfaceHigher
                    ) {
                        Spacer(Modifier.weight(1f))
                        appBarItems.forEachIndexed { index, item ->
                            NavigationRailItem(
                                selected = item.selected,
                                onClick = {
                                    item.clickAction()
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(item.imageResource),
                                        contentDescription = null
                                    )
                                },
                                label = { Text(item.label) }
                            )
                        }
                        Spacer(Modifier.weight(1f))
                    }
                    Box(
                        modifier =
                            Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(MaterialTheme.colorScheme.outline)
                                .align(Alignment.CenterEnd)
                    )
                }
                content(innerPadding)
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 1280
)
fun LazyPizzaNavigationRailPreview() {
    LazyPizzaTheme {
        NavigationRailScaffold(
            title = "Cart",
            appBarItems = previewBottomNavItems
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
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