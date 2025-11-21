package com.joshayoung.lazypizza.order.domain.network.models

data class OrderDto(
    val userId: String,
    val orderNumber: String,
    val pickupTime: String,
    val items: String,
    val totalAmount: String,
    val status: String,
    val createdAt: String
)