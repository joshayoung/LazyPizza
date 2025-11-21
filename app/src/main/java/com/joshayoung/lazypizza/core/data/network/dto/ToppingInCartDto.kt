package com.joshayoung.lazypizza.core.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ToppingInCartDto(
    val toppingId: Long,
    val remoteId: Long,
    val name: String,
    val price: String,
    val imageUrl: String,
    val productId: Int
//    val numberOfToppings: Int = 0
)