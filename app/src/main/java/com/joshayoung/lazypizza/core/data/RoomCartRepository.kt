package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCartRepository(
    private var cartLocalDataSource: CartLocalDataSource
) : CartRepository {
    override suspend fun addProductToCart(product: Product) {
        TODO("Not yet implemented")
    }

    override fun getCart(): Flow<CartEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun removeProductFromCart(product: Product) {
        TODO("Not yet implemented")
    }

    override fun getProducts(): Flow<List<Product>> =
        cartLocalDataSource.getProducts().map { productEntityList ->
            productEntityList.map { productEntity ->
                productEntity.toProduct()
            }
        }
}