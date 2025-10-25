package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.CartProductId
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.ProductEntity
import com.joshayoung.lazypizza.core.domain.models.ProductEntityWithCartStatus
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import kotlinx.coroutines.flow.Flow

class RoomLocalDataSource(
    private var cartDao: CartDao
) : LocalDataSource {
    override suspend fun getAllProducts(): List<ProductEntity> = cartDao.getAllProducts()

    override suspend fun addProductToCart(productId: Long?) {
        if (productId == null) {
            return
        }

        cartDao.insertProductId(
            CartProductId(
                cartPivotId = 1,
                productId = productId
            )
        )
    }

    override suspend fun removeAllFromCart(productId: Long?) {
        if (productId == null) {
            return
        }

        cartDao.deleteAll(productId)
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

    override suspend fun createCartForUser(cartId: Long) {
        cartDao.addCart(
            CartEntity(
                cartId,
                "Pizza Orders"
            )
        )
    }

    override suspend fun doesCartExist(cartId: Long): Boolean {
        return cartDao.doesCartExist(cartId)
    }

    override suspend fun allProductsWithCartItems(): List<ProductEntityWithCartStatus> =
        cartDao.allProductsWithCartItems()

    override suspend fun getNumberProductsInCart(cartId: Long): Flow<Int> {
        return cartDao.getNumberProductsInCart(cartId)
    }

    override suspend fun removeProductFromCart(product: Product) {
        val item = cartDao.getProductInCart(product.id)
        if (item != null) {
            cartDao.deleteCartItem(item)
        }
    }
}