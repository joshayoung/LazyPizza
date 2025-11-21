package com.joshayoung.lazypizza.order.presentation.mappers

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.order.domain.models.Order
import com.joshayoung.lazypizza.order.domain.models.OrderStatus
import com.joshayoung.lazypizza.order.presentation.models.OrderUi
import com.joshayoung.lazypizza.order.presentation.models.ProductWithCountUi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun Order.toOrderUi(): OrderUi {
    return OrderUi(
        number = number,
        date = formatDate(date),
        productsWithCount = getProductWithCounts(products),
        status = status,
        total = total,
        userId = userId,
        pickupTime = pickupTime
    )
}

private fun formatDate(date: String): String {
    val dateTimeWithZone = ZonedDateTime.parse(date)
    val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")

    return dateTimeWithZone.format(formatter)
}

private fun getProductWithCounts(products: List<Product>): List<ProductWithCountUi> {
    return products.groupBy { it.id }.map { (key, value) ->
        ProductWithCountUi(
            name = value.first().name,
            number = value.count().toString()
        )
    }
}

// TODO: Use this in UI layer:
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
