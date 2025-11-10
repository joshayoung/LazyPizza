package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.data.database.dto.ProductInCartDto
import com.joshayoung.lazypizza.core.data.database.dto.ToppingInCartDto
import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCartEntity
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getAllProducts(): List<ProductEntity>

    suspend fun clearCartForUser(user: String?)

    suspend fun getAllToppings(): List<ToppingEntity>

    suspend fun addProductToCart(productId: Long?): Long?

    suspend fun getProductInCart(lastLineNumber: Long): ProductsInCartEntity?

    suspend fun productsInCartWithToppings(): Flow<List<ProductInCartDto>>

    suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCartDto>

    suspend fun deleteCartItem(item: ProductsInCartEntity)

    suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCartDto>>

    suspend fun sidesNotInCart(): Flow<List<ProductEntity>>

    suspend fun removeAllFromCart(lineNumber: Long)

    suspend fun upsertCart(cartEntity: CartEntity): Result<CartEntity, DataError.Local>

    suspend fun upsertTopping(toppingEntity: ToppingEntity)

    suspend fun upsertProduct(productEntity: ProductEntity): Result<ProductEntity, DataError.Local>

    suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    )

    suspend fun insertToppingId(toppingsInCartEntity: ToppingsInCartEntity)

    suspend fun insertProductId(productsInCartEntity: ProductsInCartEntity): Long

    suspend fun doesCartExist(cartId: Long): Boolean

    fun allProductsWithCartItems(): Flow<List<ProductInCartDto>>

    fun getNumberProductsInCart(cartId: Long): Flow<Int>

    suspend fun removeProductFromCart(product: Product)

    suspend fun getProduct(productId: String): Product

    suspend fun transferCart(
        owner: String?,
        user: String?
    )
}