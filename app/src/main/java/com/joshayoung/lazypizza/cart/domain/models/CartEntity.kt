package com.joshayoung.lazypizza.cart.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val cartId: Long
)