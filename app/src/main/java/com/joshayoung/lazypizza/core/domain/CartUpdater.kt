package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.data.network.models.ToppingInCartDto
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface CartUpdater {
    suspend fun insertProductWithToppings(
        cartId: Long,
        productId: Long,
        toppings: List<ToppingInCartDto>
    )

    suspend fun insertProductWithToppings(
        product: Product?,
        toppingsUi: List<ToppingUi>
    )

    suspend fun productsInCart(): Flow<List<InCartItemUi>>

    fun getTotal(inCartItems: List<InCartItemUi>): BigDecimal

    suspend fun removeItemFromCart(lastLineNumber: Long?)

    suspend fun removeAllFromCart(lineNumbers: List<Long?>)
}