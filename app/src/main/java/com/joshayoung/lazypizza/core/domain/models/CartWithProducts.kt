package com.joshayoung.lazypizza.core.domain.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class CartWithProducts(
    @Embedded val cartEntity: CartEntity,
    @Relation(
        parentColumn = "cartId",
        entityColumn = "productId"
    )
    val productEntities: List<ProductEntity>
)
