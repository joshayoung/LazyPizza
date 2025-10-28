package com.joshayoung.lazypizza.core.domain.models

import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity

data class CartItem(
//    val id: String,
//    val lineItemId: Long? = null,
//    val localId: Long? = null,
    val name: String,
    val price: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val type: String,
    // TODO: Update to domain model:
    val numberInCart: Int = 0
)
