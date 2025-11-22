package com.joshayoung.lazypizza.order.domain.network

import com.joshayoung.lazypizza.cart.domain.models.OrderRequest
import com.joshayoung.lazypizza.order.domain.network.models.OrderDto

interface OrderRemoteDataSource {
    suspend fun placeOrder(orderRequest: OrderRequest): String?

    suspend fun getOrderInfo(id: String): OrderDto?

    suspend fun getOrders(
        user: String,
        table: String
    ): List<OrderDto>
}