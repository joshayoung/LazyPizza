package com.joshayoung.lazypizza.order.data.mappers

import com.joshayoung.lazypizza.cart.domain.models.OrderRequest
import com.joshayoung.lazypizza.order.data.database.entity.OrderEntity

fun OrderRequest.toOrderEntity(): OrderEntity {
    return OrderEntity(
        userId = userId,
        orderNumber = orderNumber,
        pickupTime = pickupTime,
        items = items,
        totalAmount = checkoutPrice,
        status = status
    )
}