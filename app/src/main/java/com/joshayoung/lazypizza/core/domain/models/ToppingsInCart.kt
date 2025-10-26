package com.joshayoung.lazypizza.core.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "toppings_in_cart",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
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
data class ToppingsInCart(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val toppingId: Long,
    val cartId: Long
)
