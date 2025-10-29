package com.joshayoung.lazypizza.core.data.database.entity

data class ProductInCartEntityNoToppings(
    var lineItemId: Long,
    val productId: Long,
    val remoteId: String? = null,
    val name: String? = null,
    val price: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val type: String? = null
//    val numberInCart: Int? = null
)
