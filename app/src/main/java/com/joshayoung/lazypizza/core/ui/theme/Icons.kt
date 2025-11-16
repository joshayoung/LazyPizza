package com.joshayoung.lazypizza.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.joshayoung.lazypizza.R

val GrayPhone: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.gray_phone)

val PizzaHeader: Painter
    @Composable
    get() = painterResource(R.drawable.pizza_header)

val PizzaLogo: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.pizza_logo)

val MinusIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.minus)

val PlusIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.plus)

val SearchIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.search)

val TrashIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.trash)

val BackIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.back)

val MenuIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.book)

val LogoutIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logout)

val UserIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.user)

val CartIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.cart)

val HistoryIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.history)

val UpIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.up)

val DownIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.down)
