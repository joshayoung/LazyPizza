package com.joshayoung.lazypizza.core.data.database.dto

data class ProductInCartDto(
    var lineItemId: Long? = null,
    val remoteId: String,
    val productId: Long,
    val nameWithToppingIds: String? = null,
    val name: String,
    val price: String,
    val description: String,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val type: String? = null,
    val numberInCart: Int? = null
)