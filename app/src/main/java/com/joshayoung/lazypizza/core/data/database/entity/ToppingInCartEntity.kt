package com.joshayoung.lazypizza.core.data.database.entity

import kotlinx.serialization.Serializable

@Serializable
data class ToppingInCartEntity(
    val toppingId: Long,
    val remoteId: Long,
    val name: String,
    val price: String,
    val imageUrl: String,
    val productId: Int,
    val numberOfToppings: Int
)