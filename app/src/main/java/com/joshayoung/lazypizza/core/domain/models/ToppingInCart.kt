package com.joshayoung.lazypizza.core.domain.models

data class ToppingInCart(
    val toppingId: Long,
    val remoteId: Long,
    val name: String,
    val price: String,
    val imageUrl: String,
    val productId: Int
)
