package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.CartProductId
import com.joshayoung.lazypizza.core.domain.models.ProductEntity
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import kotlinx.coroutines.flow.Flow

class RoomLocalDataSource(
    private var cartDao: CartDao
) : LocalDataSource {
    override fun getProducts(): Flow<List<ProductEntity>> = cartDao.getProduct()

    override suspend fun getAllProducts(): List<ProductEntity> = cartDao.getAllProducts()

    override suspend fun addProductToCart() {
        val ttt =
            cartDao.insertProductId(
                CartProductId(
                    cartPivotId = 1,
                    productId = 1
                )
            )
        val tt = cartDao.exists(cartId = 1, productId = 1)
        val t = cartDao.getCartProducts(cartPivotId = 1).first()
        val pro = cartDao.getProductById(t)
        println()
    }

    override suspend fun upsertCart(cartEntity: CartEntity): Result<CartEntity, DataError.Local> {
        cartDao.addCart(cartEntity)

        // TODO: This return is probably not correct:
        return Result.Success(data = cartEntity)
    }

    override suspend fun upsertProduct(
        productEntity: ProductEntity
    ): Result<ProductEntity, DataError.Local> {
        cartDao.upsertProduct(productEntity)

        // TODO: This return is probably not correct:
        return Result.Success(data = productEntity)
    }
}