package com.joshayoung.lazypizza.core.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "toppings_in_cart",
    foreignKeys = [
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
        ),
        ForeignKey(
            entity = ProductsInCartEntity::class,
            parentColumns = ["id"],
            childColumns = ["lineItemNumber"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ToppingsInCartEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lineItemNumber: Long,
    val toppingId: Long,
    val cartId: Long
)
