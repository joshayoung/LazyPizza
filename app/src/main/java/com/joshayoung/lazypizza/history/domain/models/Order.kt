package com.joshayoung.lazypizza.history.domain.models

import com.joshayoung.lazypizza.cart.domain.models.Ordered

data class Order(
    val number: String,
    val date: String,
    // TODO: Convert to InCartItem:
    val items: List<Ordered>,
    val status: OrderStatus,
    val total: String,
    val userId: String,
    val pickupTime: String
)
