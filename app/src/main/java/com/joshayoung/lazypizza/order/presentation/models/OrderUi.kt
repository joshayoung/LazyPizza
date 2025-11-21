package com.joshayoung.lazypizza.order.presentation.models

data class OrderUi(
    val number: String,
    val date: String,
    val productsWithCount: List<ProductWithCountUi>,
    val status: OrderStatusUi,
    val total: String,
    val userId: String,
    val pickupTime: String
)