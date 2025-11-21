package com.joshayoung.lazypizza.order.domain

import com.joshayoung.lazypizza.order.domain.models.Order

interface OrderRepository {
    suspend fun getOrderInfo(orderNumber: String): Order?

    suspend fun getOrdersFor(user: String): List<Order>

    suspend fun placeOrder(
        userId: String,
        orderNumber: String,
        pickupTime: String,
        items: String,
        checkoutPrice: String,
        status: String
    ): String?
}