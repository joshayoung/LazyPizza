package com.joshayoung.lazypizza.core.domain.models

data class Cart(
    val productIds: List<String>,
    val purchaseTotal: Double
)
