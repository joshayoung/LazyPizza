package com.joshayoung.lazypizza.cart.domain.models

import androidx.room.Embedded
import androidx.room.Relation

data class CartWithProducts(
    @Embedded val cartEntity: CartEntity,
    @Relation(
        parentColumn = "cartId",
        entityColumn = "productId"
    )
    val productEntities: List<ProductEntity>
)
