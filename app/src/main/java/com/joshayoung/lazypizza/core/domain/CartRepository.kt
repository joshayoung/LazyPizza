package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductWithCartStatusEntity
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductToCart(product: Product): Long?

    fun getCart(): Flow<CartEntity>

    suspend fun removeAllFromCart(lineNumber: Long)

    suspend fun getProducts(): Flow<List<Product>>

    suspend fun getToppings(): Flow<List<Topping>>

    suspend fun updateLocalWithRemote(reload: Boolean = false)

    suspend fun updateLocalToppingsWithRemote(reload: Boolean = false)

    suspend fun removeProductFromCart(product: Product)

    suspend fun getAllProducts(): List<Product>

    suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    )

    fun allProductsWithCartItems(): Flow<List<ProductWithCartStatusEntity>>

    suspend fun getNumberProductsInCart(cartId: Long): Flow<Int>

    suspend fun getProduct(productId: String): Product
}