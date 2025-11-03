package com.joshayoung.lazypizza.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItemUi
import com.joshayoung.lazypizza.core.ui.theme.CartIcon
import com.joshayoung.lazypizza.core.ui.theme.HistoryIcon
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaColors
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.MenuIcon
import com.joshayoung.lazypizza.core.ui.theme.primary8
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.core.ui.theme.textPrimary

@Composable
fun PizzaBottomBar(
    cartItems: Int = 0,
    bottomNavItemUis: List<BottomNavItemUi>
) {
    var count by remember { mutableIntStateOf(0) }

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
                bottomNavItemUis.forEachIndexed { index, item ->
                    if (index == 1 && count > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = LazyPizzaColors.textOnPrimary
                                ) {
//                                    Text(cartItems.toString())
                                    Text(count.toString())
                                }
                            }
                        ) {
                            NavItem(
                                item.label,
                                item.clickAction,
                                item.selected,
                                item.imageVector
                            )
                        }
                    } else {
                        NavItem(item.label, item.clickAction, item.selected, item.imageVector)
                    }
                }
            }
        }
    )
}

@Composable
fun NavItem(
    label: String,
    clickAction: () -> Unit,
    selected: Boolean,
    imageVector: ImageVector
) {
    var outlineBackground = Color.Transparent
    var tint = MaterialTheme.colorScheme.onSecondary
    if (selected) {
        outlineBackground = MaterialTheme.colorScheme.primary8
        tint = MaterialTheme.colorScheme.primary
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier =
            Modifier
                .clickable {
                    clickAction()
                }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .background(outlineBackground, shape = CircleShape)
                    .padding(4.dp)
                    .size(28.dp)
        ) {
            Icon(
                tint = tint,
                imageVector = imageVector,
                contentDescription = null,
                modifier =
                    Modifier
                        .height(16.dp)
                        .width(16.dp)
            )
        }

        Text(
            label,
            style = MaterialTheme.typography.titleSmall,
            color = LazyPizzaColors.textSecondary,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
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
            PizzaBottomBar(
                cartItems = 103,
                bottomNavItemUis =
                    listOf(
                        BottomNavItemUi(
                            label = "Menu",
                            selected = true,
                            clickAction = { },
                            imageVector = MenuIcon
                        ),
                        BottomNavItemUi(
                            label = "Cart",
                            selected = false,
                            clickAction = { },
                            imageVector = CartIcon
                        ),
                        BottomNavItemUi(
                            label = "History",
                            selected = false,
                            clickAction = { },
                            imageVector = HistoryIcon
                        )
                    )
            )
        }
    }
}