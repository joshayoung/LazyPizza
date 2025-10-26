package com.joshayoung.lazypizza.core.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "product_toppings",
    primaryKeys = ["productId", "toppingId"],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productId"],
            childColumns = ["toppingId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ToppingEntity::class,
            parentColumns = ["toppingId"],
            childColumns = ["toppingId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CartEntity::class,
            parentColumns = ["cartId"],
            childColumns = ["cartId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductToppings(
    val productId: Long,
    val toppingId: Long,
    val cartId: Long
)
