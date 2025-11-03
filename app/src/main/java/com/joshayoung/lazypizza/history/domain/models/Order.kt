package com.joshayoung.lazypizza.history.domain.models

import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi

data class Order(
    val number: String,
    val date: String,
    // TODO: Convert to InCartItem:
    val items: List<InCartItemUi>,
    val status: OrderStatus,
    val total: String
)
