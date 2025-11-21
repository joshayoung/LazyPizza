package com.joshayoung.lazypizza.order.presentation.mappers

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.order.domain.models.Order
import com.joshayoung.lazypizza.order.presentation.models.OrderStatusUi
import com.joshayoung.lazypizza.order.presentation.models.OrderUi
import com.joshayoung.lazypizza.order.presentation.models.ProductWithCountUi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun Order.toOrderUi(): OrderUi {
    return OrderUi(
        number = number,
        date = formatDate(date),
        productsWithCount = getProductWithCounts(products),
        status = getStatus(status),
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

private fun getStatus(status: String): OrderStatusUi {
    return when (status) {
        "inProgress" -> {
            OrderStatusUi.InProgress
        }
        "completed" -> {
            OrderStatusUi.Completed
        }
        else -> {
            OrderStatusUi.Unknown
        }
    }
}
