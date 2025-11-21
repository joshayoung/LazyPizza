package com.joshayoung.lazypizza.order.domain

import com.joshayoung.lazypizza.order.domain.models.OrderWithProducts

interface OrderProcessor {
    suspend fun getOrdersFor(user: String): List<OrderWithProducts>

    suspend fun getOrderInfo(orderNumber: String): OrderWithProducts?
}
