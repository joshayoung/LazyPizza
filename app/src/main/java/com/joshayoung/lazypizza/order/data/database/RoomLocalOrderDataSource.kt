package com.joshayoung.lazypizza.order.data.database

import com.joshayoung.lazypizza.order.data.database.entity.OrderEntity
import com.joshayoung.lazypizza.order.domain.LocalOrderDataSource

class RoomLocalOrderDataSource(
    private val orderDao: OrderDao
) : LocalOrderDataSource {
    override suspend fun insertOrder(orderEntity: OrderEntity) {
        orderDao.upsertOrder(orderEntity)
    }

    override suspend fun getOrders(userId: String): List<OrderEntity> {
        return orderDao.getAllOrdersForUser(userId)
    }
}