package com.joshayoung.lazypizza.history.domain.models

import com.joshayoung.lazypizza.core.domain.models.Product

data class Order(
    val number: String,
    val date: String,
    // TODO: Convert to InCartItem:
    val products: List<Product>,
    val status: OrderStatus,
    val total: String,
    val userId: String,
    val pickupTime: String
)
