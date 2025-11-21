package com.joshayoung.lazypizza.order.data

import com.joshayoung.lazypizza.menu.domain.MenuRepository
import com.joshayoung.lazypizza.order.domain.OrderProcessor
import com.joshayoung.lazypizza.order.domain.OrderRepository
import com.joshayoung.lazypizza.order.domain.models.OrderWithProducts

class OrderProcessorImpl(
    private var menuRepository: MenuRepository,
    private var orderRepository: OrderRepository
) : OrderProcessor {
    override suspend fun getOrdersFor(user: String): List<OrderWithProducts> {
        val orders = orderRepository.getOrdersFor(user)

        val orderWithProducts =
            orders.map { order ->
                val products =
                    order.products.map { productWithToppings ->
                        menuRepository.getProduct(productWithToppings.productRemoteId)
                    }
                OrderWithProducts(
                    number = order.number,
                    date = order.date,
                    products = products,
                    status = order.status,
                    total = order.total,
                    userId = order.userId,
                    pickupTime = order.pickupTime
                )
            }

        return orderWithProducts
    }

    override suspend fun getOrderInfo(orderNumber: String): OrderWithProducts? {
        val order = orderRepository.getOrderInfo(orderNumber)
        if (order != null) {
            val products =
                order.products.map { order ->
                    menuRepository.getProduct(order.productRemoteId)
                }

            return OrderWithProducts(
                number = order.number,
                date = order.date,
                products = products,
                status = order.status,
                total = order.total,
                userId = order.userId,
                pickupTime = order.pickupTime
            )
        }

        return null
    }
}