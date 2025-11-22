package com.joshayoung.lazypizza.order.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val orderId: Long = 0,
    val userId: String,
    val orderNumber: String,
    val pickupTime: String,
    val items: String,
    val totalAmount: String,
    val status: String,
    val createdAt: Long = System.currentTimeMillis()
)
