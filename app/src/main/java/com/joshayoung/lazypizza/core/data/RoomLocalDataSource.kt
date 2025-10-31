package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductWithCartStatusEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCart
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCart
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import kotlinx.coroutines.flow.Flow

class RoomLocalDataSource(
    private var cartDao: CartDao
) : LocalDataSource {
    override suspend fun getAllProducts(): List<ProductEntity> {
        return cartDao.getAllProducts()
    }

    override suspend fun getAllToppings(): List<ToppingEntity> {
        return cartDao.getAllToppings()
    }

    override suspend fun addProductToCart(productId: Long?): Long? {
        if (productId == null) {
            return null
        }

        val lineItemNumber =
            cartDao.insertProductId(
                ProductsInCart(
                    cartId = 1,
                    productId = productId
                )
            )

        return lineItemNumber
    }

    override suspend fun getProductInCart(lastLineNumber: Long): ProductsInCart? {
        return cartDao.getProductInCart(lastLineNumber)
    }

    override suspend fun productsInCartWithToppings(): Flow<List<ProductInCartEntity>> {
        return cartDao
            .productsInCartWithToppings()
    }

    override suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCartEntity> {
        return cartDao.getToppingForProductInCart(lineItemId)
    }

    override suspend fun deleteCartItem(item: ProductsInCart) {
        cartDao.deleteCartItem(item)
    }

    override suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCartEntity>> {
        return cartDao
            .productsInCartWithNoToppings()
    }

    override suspend fun sidesNotInCart(): Flow<List<ProductEntity>> {
        return cartDao.sidesNotInCart()
    }

    override suspend fun removeAllFromCart(lineNumber: Long) {
        cartDao.deleteItemFromCart(lineNumber)
    }

    override suspend fun upsertCart(cartEntity: CartEntity): Result<CartEntity, DataError.Local> {
        cartDao.addCart(cartEntity)

        // TODO: This return is probably not correct:
        return Result.Success(data = cartEntity)
    }

    override suspend fun upsertTopping(toppingEntity: ToppingEntity) {
        cartDao.upsertTopping(toppingEntity)
    }

    override suspend fun upsertProduct(
        productEntity: ProductEntity
    ): Result<ProductEntity, DataError.Local> {
        cartDao.upsertProduct(productEntity)

        // TODO: This return is probably not correct:
        return Result.Success(data = productEntity)
    }

    override suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    ) {
        cartDao.addCart(
            CartEntity(
                cartId,
                theUser
            )
        )
    }

    override suspend fun insertToppingId(toppingsInCart: ToppingsInCart) {
        cartDao.insertToppingId(
            toppingsInCart
        )
    }

    override suspend fun insertProductId(productsInCart: ProductsInCart): Long {
        return cartDao.insertProductId(
            productsInCart
        )
    }

    override suspend fun doesCartExist(cartId: Long): Boolean {
        return cartDao.doesCartExist(cartId)
    }

    override fun allProductsWithCartItems(): Flow<List<ProductWithCartStatusEntity>> =
        cartDao.allProductsWithCartItems()

    override suspend fun getNumberProductsInCart(cartId: Long): Flow<Int> {
        return cartDao.getNumberProductsInCart(cartId)
    }

    // Getting a null value when trying to remove from cart and the reduction in quantity is therefore not changing the price correctly:
    override suspend fun removeProductFromCart(product: Product) {
        val item = cartDao.getProductInCart(product.lineItemId ?: 0)
        if (item != null) {
            cartDao.deleteCartItem(item)
        }
    }

    override suspend fun getProduct(productId: String): Product {
        return cartDao.getProduct(productId).toProduct()
    }
}