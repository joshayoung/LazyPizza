package com.joshayoung.lazypizza.cart.domain.models

data class OrderRequest(
    val userId: String,
    val orderNumber: String,
    val pickupTime: String,
    val items: String,
    val checkoutPrice: String,
    val status: String
)
