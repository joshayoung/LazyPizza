package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.data.database.ProductDao
import com.joshayoung.lazypizza.core.data.database.ToppingDao
import com.joshayoung.lazypizza.core.data.network.dto.ProductInCartDto
import com.joshayoung.lazypizza.core.data.network.dto.ToppingInCartDto
import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCartEntity
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import kotlinx.coroutines.flow.Flow

class RoomLocalDataSource(
    private var cartDao: CartDao,
    private var productDao: ProductDao,
    private var toppingDao: ToppingDao
) : LocalDataSource {
    override suspend fun getAllProducts(): List<ProductEntity> {
        return productDao.getAllProducts()
    }

    override suspend fun clearCartForUser(user: String?) {
        if (user != null) {
            val cartId = cartDao.getCartIdForUser(user)
            cartDao.deleteAllItemsFor(cartId.cartId)
        }
    }

    override suspend fun getAllToppings(): List<ToppingEntity> {
        return toppingDao.getAllToppings()
    }

    override suspend fun addProductToCart(productId: Long?): Long? {
        if (productId == null) {
            return null
        }

        val lineItemNumber =
            cartDao.insertProductId(
                ProductsInCartEntity(
                    cartId = 1,
                    productId = productId
                )
            )

        return lineItemNumber
    }

    override suspend fun getProductInCart(lastLineNumber: Long): ProductsInCartEntity? {
        return cartDao.getProductInCart(lastLineNumber)
    }

    override suspend fun productsInCartWithToppings(): Flow<List<ProductInCartDto>> {
        return cartDao
            .productsInCartWithToppings()
    }

    override suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCartDto> {
        return cartDao.getToppingForProductInCart(lineItemId)
    }

    override suspend fun deleteCartItem(item: ProductsInCartEntity) {
        cartDao.deleteCartItem(item)
    }

    override suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCartDto>> {
        return cartDao
            .productsInCartWithNoToppings()
    }

    override suspend fun sidesNotInCart(): Flow<List<ProductEntity>> {
        return productDao.sidesNotInCart()
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
        toppingDao.upsertTopping(toppingEntity)
    }

    override suspend fun upsertProduct(
        productEntity: ProductEntity
    ): Result<ProductEntity, DataError.Local> {
        productDao.upsertProduct(productEntity)

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

    override suspend fun insertToppingId(toppingsInCartEntity: ToppingsInCartEntity) {
        cartDao.insertToppingId(
            toppingsInCartEntity
        )
    }

    override suspend fun insertProductId(productsInCartEntity: ProductsInCartEntity): Long {
        return cartDao.insertProductId(
            productsInCartEntity
        )
    }

    override suspend fun doesCartExist(cartId: Long): Boolean {
        return cartDao.doesCartExist(cartId)
    }

    override fun allProductsWithCartItems(): Flow<List<ProductInCartDto>> =
        cartDao.allProductsWithCartItems()

    override fun getNumberProductsInCart(cartId: Long): Flow<Int> {
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
        return productDao.getProduct(productId).toProduct()
    }

    override suspend fun transferCart(
        owner: String?,
        user: String?
    ) {
        // TODO: Pass this ID In:
        if (owner == null || user == null) {
            return
        }

        val getOwnerCart = cartDao.getCart(owner)
        cartDao.updateCart(
            CartEntity(
                getOwnerCart.cartId,
                user
            )
        )
    }
}