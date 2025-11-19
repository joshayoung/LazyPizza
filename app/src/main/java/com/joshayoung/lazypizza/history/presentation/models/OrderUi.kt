package com.joshayoung.lazypizza.history.presentation.models

import com.joshayoung.lazypizza.history.domain.models.OrderStatus

data class OrderUi(
    val number: String,
    val date: String,
    // TODO: Convert to ProductUi:
    val productsWithCount: List<ProductWithCountUi>,
    val status: OrderStatus,
    val total: String,
    val userId: String,
    val pickupTime: String
)