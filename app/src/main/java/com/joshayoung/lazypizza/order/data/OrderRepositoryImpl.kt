package com.joshayoung.lazypizza.order.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.cart.domain.models.OrderDto
import com.joshayoung.lazypizza.cart.domain.models.OrderRequest
import com.joshayoung.lazypizza.cart.domain.models.Ordered
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.menu.domain.MenuRepository
import com.joshayoung.lazypizza.order.domain.OrderRepository
import com.joshayoung.lazypizza.order.domain.models.Order
import com.joshayoung.lazypizza.order.domain.models.OrderStatus
import kotlinx.serialization.json.Json

class OrderRepositoryImpl(
    private var cartRemoteDataSource: CartRemoteDataSource,
    private var cartRepository: CartRepository,
    private var menuRepository: MenuRepository
) : OrderRepository {
    override suspend fun getOrdersFor(user: String): List<Order> {
        val orderDtos =
            cartRemoteDataSource
                .getOrders(
                    user,
                    BuildConfig.ORDERS_COLLECTION_ID
                )

        val orders =
            orderDtos.map { orderDto ->
                val ordered = Json.decodeFromString<List<Ordered>>(orderDto.items)

                // TODO: Also return the toppings:
                val products =
                    ordered.map { order ->
                        // TODO: This needs to be done in a use case instead of having two repos:
                        menuRepository.getProduct(order.productRemoteId)
                    }
                Order(
                    number = orderDto.orderNumber,
                    date = orderDto.createdAt,
                    products = products,
                    status = getStatus(orderDto.status),
                    total = orderDto.totalAmount,
                    userId = orderDto.userId,
                    pickupTime = orderDto.pickupTime
                )
            }

        return orders
    }

    override suspend fun getOrderInfo(orderNumber: String): OrderDto? {
        return cartRemoteDataSource.getOrderInfo(orderNumber)
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

    override suspend fun placeOrder(
        userId: String,
        orderNumber: String,
        pickupTime: String,
        items: String,
        checkoutPrice: String,
        status: String
    ): String? {
        val orderRequest =
            OrderRequest(
                userId = userId,
                orderNumber = orderNumber,
                pickupTime = pickupTime,
                items = items,
                checkoutPrice = checkoutPrice,
                status = status
            )
        return cartRemoteDataSource.placeOrder(orderRequest)
    }
}