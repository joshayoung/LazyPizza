package com.joshayoung.lazypizza.order.presentation.mappers

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.order.domain.models.OrderWithProducts
import com.joshayoung.lazypizza.order.presentation.models.OrderStatusUi
import com.joshayoung.lazypizza.order.presentation.models.OrderUi
import com.joshayoung.lazypizza.order.presentation.models.ProductWithCountUi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun OrderWithProducts.toOrderUi(): OrderUi {
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
    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val outputFormat = DateTimeFormatter.ofPattern("MMMM dd, HH:mm")
    val theDateTime = LocalDateTime.parse(date, inputFormat)

    return theDateTime.format(outputFormat)
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
