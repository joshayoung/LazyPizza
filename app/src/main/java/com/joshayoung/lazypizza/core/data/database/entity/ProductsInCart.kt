package com.joshayoung.lazypizza.core.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products_in_cart",
    foreignKeys = [
        ForeignKey(
            entity = CartEntity::class,
            parentColumns = ["cartId"],
            childColumns = ["cartPivotId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("cartPivotId")]
)
data class ProductsInCart(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cartPivotId: Long,
    val productId: Long
)
