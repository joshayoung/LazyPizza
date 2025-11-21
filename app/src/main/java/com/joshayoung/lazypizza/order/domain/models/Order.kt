package com.joshayoung.lazypizza.order.domain.models

import com.joshayoung.lazypizza.core.domain.models.Product

data class Order(
    val number: String,
    val date: String,
    val products: List<Product>,
    val status: String,
    val total: String,
    val userId: String,
    val pickupTime: String
)
