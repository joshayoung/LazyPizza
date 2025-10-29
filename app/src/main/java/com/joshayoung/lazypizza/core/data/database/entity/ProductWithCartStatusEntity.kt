package com.joshayoung.lazypizza.core.data.database.entity

data class ProductWithCartStatusEntity(
    val lineItemId: Long,
    val productId: Long,
    val remoteId: String,
    val name: String,
    val price: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val type: String,
    val numberInCart: Int
)