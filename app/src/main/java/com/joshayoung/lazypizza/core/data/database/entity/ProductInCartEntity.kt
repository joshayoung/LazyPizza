package com.joshayoung.lazypizza.core.data.database.entity

data class ProductInCartEntity(
    var lineItemId: Long? = null,
    val productId: Long,
    val remoteId: String,
    val name: String,
    val price: String,
    val description: String,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val type: String? = null
)
