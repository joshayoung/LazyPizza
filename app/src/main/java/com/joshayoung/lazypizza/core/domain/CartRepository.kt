package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductWithCartStatusEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCart
import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCart
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductToCart(product: Product): Long?

    suspend fun removeAllFromCart(lineNumber: Long)

    suspend fun getToppings(): List<Topping>

    suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCartEntity>

    suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCartEntity>>

    suspend fun productsInCartWithToppings(): Flow<List<ProductInCartEntity>>

    suspend fun insertProductId(productsInCart: ProductsInCart): Long

    suspend fun updateLocalWithRemote(reload: Boolean = false)

    suspend fun updateLocalToppingsWithRemote(reload: Boolean = false)

    suspend fun insertToppingId(toppingsInCart: ToppingsInCart)

    suspend fun getProductInCart(lastLineNumber: Long): ProductsInCart?

    suspend fun deleteCartItem(item: ProductsInCart)

    suspend fun sidesNotInCart(): Flow<List<ProductEntity>>

    suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    )

    fun allProductsWithCartItems(): Flow<List<ProductWithCartStatusEntity>>

    suspend fun getNumberProductsInCart(cartId: Long): Flow<Int>

    suspend fun getProduct(productId: String): Product
}