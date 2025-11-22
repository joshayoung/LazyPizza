package com.joshayoung.lazypizza.order.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.lazypizza.order.data.database.entity.OrderEntity

@Dao
interface OrderDao {
    @Upsert
    suspend fun upsertOrder(orderEntity: OrderEntity)

    @Query("SELECT * FROM `order` WHERE userId = :userId")
    suspend fun getAllOrdersForUser(userId: String): List<OrderEntity>
}