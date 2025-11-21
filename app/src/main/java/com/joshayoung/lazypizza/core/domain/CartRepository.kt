package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.data.database.dto.ProductInCartDto
import com.joshayoung.lazypizza.core.data.database.dto.ToppingInCartDto
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCartEntity
import com.joshayoung.lazypizza.core.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductToCart(product: Product): Long?

    suspend fun removeAllFromCart(lineNumber: Long)

    suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCartDto>

    suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCartDto>>

    suspend fun productsInCartWithToppings(): Flow<List<ProductInCartDto>>

    suspend fun insertProductId(productsInCartEntity: ProductsInCartEntity): Long

    suspend fun updateLocalWithRemote(reload: Boolean = false)

    suspend fun updateLocalToppingsWithRemote(reload: Boolean = false)

    suspend fun insertToppingId(toppingsInCartEntity: ToppingsInCartEntity)

    suspend fun getProductInCart(lastLineNumber: Long): ProductsInCartEntity?

    suspend fun deleteCartItem(item: ProductsInCartEntity)

    suspend fun sidesNotInCart(): Flow<List<Product>>

    suspend fun transferCartTo(
        owner: String?,
        user: String?
    )

    suspend fun clearCartForUser(user: String?)

    suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    )

    fun allProductsWithCartItems(): Flow<List<ProductInCartDto>>

    fun getNumberProductsInCart(cartId: Long): Flow<Int>
}