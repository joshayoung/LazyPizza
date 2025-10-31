package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductWithCartStatusEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCart
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCart
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getAllProducts(): List<ProductEntity>

    suspend fun getAllToppings(): List<ToppingEntity>

    suspend fun addProductToCart(productId: Long?): Long?

    suspend fun getProductInCart(lastLineNumber: Long): ProductsInCart?

    suspend fun productsInCartWithToppings(): Flow<List<ProductInCartEntity>>

    suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCartEntity>

    suspend fun deleteCartItem(item: ProductsInCart)

    suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCartEntity>>

    suspend fun sidesNotInCart(): Flow<List<ProductEntity>>

    suspend fun removeAllFromCart(lineNumber: Long)

    suspend fun upsertCart(cartEntity: CartEntity): Result<CartEntity, DataError.Local>

    suspend fun upsertTopping(toppingEntity: ToppingEntity)

    suspend fun upsertProduct(productEntity: ProductEntity): Result<ProductEntity, DataError.Local>

    suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    )

    suspend fun insertToppingId(toppingsInCart: ToppingsInCart)

    suspend fun insertProductId(productsInCart: ProductsInCart): Long

    suspend fun doesCartExist(cartId: Long): Boolean

    fun allProductsWithCartItems(): Flow<List<ProductWithCartStatusEntity>>

    suspend fun getNumberProductsInCart(cartId: Long): Flow<Int>

    suspend fun removeProductFromCart(product: Product)

    suspend fun getProduct(productId: String): Product
}