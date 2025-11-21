package com.joshayoung.lazypizza.order.domain.models

import com.joshayoung.lazypizza.core.domain.models.Product

// TODO: Include the toppings in this object:
data class OrderWithProducts(
    val number: String,
    val date: String,
    val products: List<Product>,
    val status: String,
    val total: String,
    val userId: String,
    val pickupTime: String
)
