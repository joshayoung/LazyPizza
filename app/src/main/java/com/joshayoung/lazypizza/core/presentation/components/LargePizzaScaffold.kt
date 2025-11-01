package com.joshayoung.lazypizza.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItemUi
import com.joshayoung.lazypizza.core.presentation.utils.previewBottomNavItemUis
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaColors
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.primary8
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher

@Composable
fun LargePizzaScaffold(
    title: String? = null,
    cartItems: Int = 0,
    appBarItems: List<BottomNavItemUi>,
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

                            if (index == 1) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = LazyPizzaColors.textOnPrimary,
                                            modifier =
                                                Modifier
                                                    .size(20.dp)
                                        ) {
                                            Text(cartItems.toString())
                                        }
                                    }
                                ) {
                                    CustomNavigationRailItem(item)
                                }
                            } else {
                                CustomNavigationRailItem(item)
                            }
                            Spacer(Modifier.height(14.dp))
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
fun CustomNavigationRailItem(item: BottomNavItemUi) {
    var outlineBackground = Color.Transparent
    var tint = MaterialTheme.colorScheme.onSecondary
    if (item.selected) {
        outlineBackground = MaterialTheme.colorScheme.primary8
        tint = MaterialTheme.colorScheme.primary
    }
    Column(
        modifier =
            Modifier
                .clickable {
                    item.clickAction()
                },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            tint = tint,
            imageVector = item.imageVector,
            contentDescription = null,
            modifier =
                Modifier
                    .background(outlineBackground, shape = CircleShape)
                    .padding(10.dp)
                    .size(28.dp)
        )
        Text(item.label)
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
        LargePizzaScaffold(
            title = "Cart",
            appBarItems = previewBottomNavItemUis
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