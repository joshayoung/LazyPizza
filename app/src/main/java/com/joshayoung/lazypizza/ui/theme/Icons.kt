package com.joshayoung.lazypizza.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.joshayoung.lazypizza.R

val GrayPhone: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.gray_phone)

val PizzaHeader: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.pizza_header)

val PizzaLogo: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.pizza_logo)

val MinusIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.minus)

val PlusIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.plus)

val PhoneIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.phone)

val SearchIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.search)

val TrashIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.trash)
