package com.joshayoung.lazypizza.core.data.database.dto

import kotlinx.serialization.Serializable

@Serializable
data class ToppingInCartDto(
    val toppingId: Long,
    val remoteId: Long,
    val name: String,
    val price: String,
    val imageUrl: String,
    val productId: Int,
    // TODO: Remove this;
    val numberOfToppings: Int? = null
)