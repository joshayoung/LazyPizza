package com.joshayoung.lazypizza.core.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val cartId: Long = 0,
    val user: String
)
