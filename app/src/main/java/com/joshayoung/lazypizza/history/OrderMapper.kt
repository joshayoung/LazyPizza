package com.joshayoung.lazypizza.history

import com.joshayoung.lazypizza.cart.domain.models.OrderDto
import com.joshayoung.lazypizza.cart.domain.models.Ordered
import com.joshayoung.lazypizza.history.domain.models.Order
import com.joshayoung.lazypizza.history.domain.models.OrderStatus
import kotlinx.serialization.json.Json

fun OrderDto.toOrder(): Order {
    val json = Json.decodeFromString<List<Ordered>>(items)
    return Order(
        number = orderNumber,
        date = createdAt,
        items = json,
        status = getStatus(status),
        total = totalAmount,
        userId = userId,
        pickupTime = pickupTime
    )
}

private fun getStatus(status: String): OrderStatus {
    return when (status) {
        "inProgress" -> {
            OrderStatus.InProgress
        }
        "completed" -> {
            OrderStatus.Completed
        }
        else -> {
            OrderStatus.Unknown
        }
    }
}
