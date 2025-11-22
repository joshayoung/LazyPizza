package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.network.models.ProductInCartDto
import com.joshayoung.lazypizza.core.data.network.models.ToppingInCartDto
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import kotlinx.coroutines.flow.Flow

interface LocalCartDataSource {
    suspend fun clearCartForUser(user: String?)

    suspend fun removeProductFromCart(product: Product)

    suspend fun addProductToCart(productId: Long?): Long?

    suspend fun getProductInCart(lastLineNumber: Long): ProductsInCartEntity?

    suspend fun productsInCartWithToppings(): Flow<List<ProductInCartDto>>

    suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCartDto>

    suspend fun deleteCartItem(item: ProductsInCartEntity)

    suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCartDto>>

    suspend fun removeAllFromCart(lineNumber: Long)

    suspend fun upsertCart(cartEntity: CartEntity): Result<CartEntity, DataError.Local>

    suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    )

    suspend fun insertToppingId(
        lineItemNumber: Long,
        toppingId: Long,
        cartId: Long
    )

    suspend fun insertProductId(
        cartId: Long,
        productId: Long
    ): Long

    suspend fun doesCartExist(cartId: Long): Boolean

    fun allProductsWithCartItems(): Flow<List<ProductInCartDto>>

    fun getNumberProductsInCart(cartId: Long): Flow<Int>

    suspend fun transferCart(
        owner: String?,
        user: String?
    )
}