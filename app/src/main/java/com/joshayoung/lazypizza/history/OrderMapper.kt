package com.joshayoung.lazypizza.history

import com.joshayoung.lazypizza.history.domain.models.Order
import com.joshayoung.lazypizza.history.domain.models.OrderStatus
import com.joshayoung.lazypizza.history.presentation.models.OrderUi

fun Order.toOrderUi(): OrderUi {
    return OrderUi(
        number = number,
        date = date,
        products = products,
        status = status,
        total = total,
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
