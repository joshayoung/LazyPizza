package com.joshayoung.lazypizza.order.domain

import com.joshayoung.lazypizza.order.data.database.entity.OrderEntity

interface LocalOrderDataSource {
    suspend fun insertOrder(orderEntity: OrderEntity)

    suspend fun getOrders(userId: String): List<OrderEntity>
}