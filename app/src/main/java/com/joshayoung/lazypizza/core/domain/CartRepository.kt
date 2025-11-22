package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.ProductInCart
import com.joshayoung.lazypizza.core.domain.models.ToppingInCart
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductToCart(product: Product): Long?

    suspend fun removeAllFromCart(lineNumber: Long)

    suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCart>

    suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCart>>

    suspend fun productsInCartWithToppings(): Flow<List<ProductInCart>>

    suspend fun insertProductId(
        cartId: Long,
        productId: Long
    ): Long

    suspend fun insertToppingId(
        lineItemNumber: Long,
        toppingId: Long,
        cartId: Long
    )

    suspend fun getProductInCart(lastLineNumber: Long): ProductsInCartEntity?

    suspend fun deleteCartItem(item: ProductsInCartEntity)

    suspend fun transferCartTo(
        owner: String?,
        user: String?
    )

    suspend fun clearCartForUser(user: String?)

    suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    )

    fun allProductsWithCartItems(): Flow<List<ProductInCart>>

    fun getNumberProductsInCart(cartId: Long): Flow<Int>
}