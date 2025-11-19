package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.cart.domain.models.OrderDto
import com.joshayoung.lazypizza.cart.domain.models.OrderRequest
import com.joshayoung.lazypizza.cart.domain.models.Ordered
import com.joshayoung.lazypizza.core.data.database.dto.ProductInCartDto
import com.joshayoung.lazypizza.core.data.database.dto.ToppingInCartDto
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCartEntity
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductEntity
import com.joshayoung.lazypizza.core.presentation.mappers.toTopping
import com.joshayoung.lazypizza.core.presentation.mappers.toToppingEntity
import com.joshayoung.lazypizza.history.domain.models.Order
import com.joshayoung.lazypizza.history.domain.models.OrderStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class CartRepositoryImpl(
    private var localDataSource: LocalDataSource,
    private var cartRemoteDataSource: CartRemoteDataSource
) : CartRepository {
    override suspend fun addProductToCart(product: Product): Long? {
        return localDataSource.addProductToCart(
            product.localId
        )
    }

    override suspend fun removeAllFromCart(lineNumber: Long) {
        localDataSource.removeAllFromCart(
            lineNumber
        )
    }

    override suspend fun getToppings(): List<Topping> {
        val localToppings = localDataSource.getAllToppings()
        if (!localToppings.isEmpty()) {
            return localToppings.map { it.toTopping() }
        } else {
            val remoteToppings =
                cartRemoteDataSource
                    .getToppings(
                        BuildConfig.TOPPINGS_COLLECTION_ID
                    )

            remoteToppings.forEach { topping ->
                localDataSource.upsertTopping(topping.toToppingEntity())
            }

            return remoteToppings
        }
    }

    override suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCartDto> {
        return localDataSource.getToppingForProductInCart(lineItemId)
    }

    override suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCartDto>> {
        return localDataSource.productsInCartWithNoToppings()
    }

    override suspend fun productsInCartWithToppings(): Flow<List<ProductInCartDto>> {
        return localDataSource.productsInCartWithToppings()
    }

    override suspend fun insertProductId(productsInCartEntity: ProductsInCartEntity): Long {
        return localDataSource.insertProductId(productsInCartEntity)
    }

    override suspend fun updateLocalWithRemote(reload: Boolean) {
        val localProducts = localDataSource.getAllProducts()
        if (!localProducts.isEmpty() && !reload) {
            return
        }

        val remoteProducts = cartRemoteDataSource.getProducts(BuildConfig.MENU_ITEMS_COLLECTION_ID)
        remoteProducts.forEach { product ->
            localDataSource.upsertProduct(product.toProductEntity())
        }
    }

    override suspend fun updateLocalToppingsWithRemote(reload: Boolean) {
        val localToppings = localDataSource.getAllToppings()
        if (!localToppings.isEmpty() && !reload) {
            return
        }

        val remoteToppings = cartRemoteDataSource.getToppings(BuildConfig.TOPPINGS_COLLECTION_ID)
        remoteToppings.forEach { topping ->
            localDataSource.upsertTopping(topping.toToppingEntity())
        }
    }

    override suspend fun insertToppingId(toppingsInCartEntity: ToppingsInCartEntity) {
        localDataSource.insertToppingId(toppingsInCartEntity)
    }

    override suspend fun getProductInCart(lastLineNumber: Long): ProductsInCartEntity? {
        return localDataSource.getProductInCart(lastLineNumber)
    }

    override suspend fun deleteCartItem(item: ProductsInCartEntity) {
        localDataSource.deleteCartItem(item)
    }

    override suspend fun sidesNotInCart(): Flow<List<Product>> {
        return localDataSource.sidesNotInCart().map {
            it.map { item ->
                item.toProduct()
            }
        }
    }

    override suspend fun transferCartTo(
        owner: String?,
        user: String?
    ) {
        localDataSource.transferCart(owner, user)
    }

    override suspend fun clearCartForUser(user: String?) {
        localDataSource.clearCartForUser(user)
    }

    override suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    ) {
        if (localDataSource.doesCartExist(cartId)) {
            return
        }

        localDataSource.createCartForUser(cartId, theUser)
    }

    override fun allProductsWithCartItems(): Flow<List<ProductInCartDto>> {
        return localDataSource.allProductsWithCartItems()
    }

    override fun getNumberProductsInCart(cartId: Long): Flow<Int> {
        return localDataSource.getNumberProductsInCart(cartId)
    }

    // TODO: Move to a different repository:
    override suspend fun getProduct(productId: String): Product {
        return localDataSource.getProduct(productId)
    }

    override suspend fun getOrderInfo(orderNumber: String): OrderDto? {
        return cartRemoteDataSource.getOrderInfo(orderNumber)
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
                        getProduct(order.productRemoteId)
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
}