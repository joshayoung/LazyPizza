package com.joshayoung.lazypizza.history

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.history.domain.models.Order
import com.joshayoung.lazypizza.history.domain.models.OrderStatus
import com.joshayoung.lazypizza.history.presentation.models.OrderUi
import com.joshayoung.lazypizza.history.presentation.models.ProductWithCountUi

fun Order.toOrderUi(): OrderUi {
    return OrderUi(
        number = number,
        date = date,
        productsWithCount = getProductWithCounts(products),
        status = status,
        total = total,
        userId = userId,
        pickupTime = pickupTime
    )
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
