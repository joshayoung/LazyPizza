package com.joshayoung.lazypizza.core.domain.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ProductWithToppings(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "productId",
        entityColumn = "toppingId",
        associateBy = Junction(ProductToppings::class)
    )
    val books: List<ToppingEntity>
)