package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.ProductEntity
import com.joshayoung.lazypizza.core.domain.models.ProductEntityWithCartStatus
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getProducts(): Flow<List<ProductEntity>>

    suspend fun getAllProducts(): List<ProductEntity>

    suspend fun addProductToCart(productId: Long?)

    suspend fun removeProductFromCart(productId: Long?)

    suspend fun upsertCart(cartEntity: CartEntity): Result<CartEntity, DataError.Local>

    suspend fun upsertProduct(productEntity: ProductEntity): Result<ProductEntity, DataError.Local>

    suspend fun createCartForUser(cartId: Long)

    suspend fun doesCartExist(cartId: Long): Boolean

    suspend fun allProductsWithCartItems(): List<ProductEntityWithCartStatus>
}