package com.joshayoung.lazypizza.order.data

import com.joshayoung.lazypizza.cart.domain.models.OrderRequest
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.order.data.mappers.toOrderEntity
import com.joshayoung.lazypizza.order.domain.LocalOrderDataSource
import com.joshayoung.lazypizza.order.domain.OrderRepository
import com.joshayoung.lazypizza.order.domain.models.Order
import com.joshayoung.lazypizza.order.domain.models.ProductWithToppings
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Locale

class OrderRepositoryImpl(
    private var cartRemoteDataSource: CartRemoteDataSource,
    private var localOrderDataSource: LocalOrderDataSource
) : OrderRepository {
    override suspend fun getOrdersFor(user: String): List<Order> {
        val orderEntities = localOrderDataSource.getOrders(user)

        val orders =
            orderEntities.map { orderEntity ->
                val productWithToppings =
                    Json.decodeFromString<List<ProductWithToppings>>(
                        orderEntity.items
                    )

                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val createdAt: String = formatter.format(orderEntity.createdAt)

                Order(
                    number = orderEntity.orderNumber,
                    date = createdAt,
                    products = productWithToppings,
                    status = orderEntity.status,
                    total = orderEntity.totalAmount,
                    userId = orderEntity.userId,
                    pickupTime = orderEntity.pickupTime
                )
            }

        return orders
    }

    override suspend fun getOrderInfo(orderNumber: String): Order? {
        val orderDto = cartRemoteDataSource.getOrderInfo(orderNumber)
        if (orderDto != null) {
            val productWithToppings =
                Json.decodeFromString<List<ProductWithToppings>>(
                    orderDto.items
                )

            return Order(
                number = orderDto.orderNumber,
                date = orderDto.createdAt,
                products = productWithToppings,
                status = orderDto.status,
                total = orderDto.totalAmount,
                userId = orderDto.userId,
                pickupTime = orderDto.pickupTime
            )
        }

        return null
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
        val result = cartRemoteDataSource.placeOrder(orderRequest)

        if (result != null) {
            localOrderDataSource.insertOrder(orderRequest.toOrderEntity())
        }

        return result
    }
}