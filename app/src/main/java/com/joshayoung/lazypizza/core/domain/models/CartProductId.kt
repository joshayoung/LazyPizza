package com.joshayoung.lazypizza.core.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_product_ids",
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
data class CartProductId(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cartPivotId: Long,
    val productId: Long
)
