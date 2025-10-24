package com.joshayoung.lazypizza.core.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val productId: Long = 0,
    val remoteId: String,
    val name: String,
    val price: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val type: String
)