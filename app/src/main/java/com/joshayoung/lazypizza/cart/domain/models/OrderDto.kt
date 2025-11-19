package com.joshayoung.lazypizza.cart.domain.models

// TODO: Add other fields:
data class OrderDto(
    val userId: String,
    val orderNumber: String,
    val pickupTime: String,
    val items: String,
    val totalAmount: String,
    // TODO: Make enum:
    val status: String,
    val createdAt: String
)