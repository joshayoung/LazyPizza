package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.data.network.dto.ToppingInCartDto
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi
import kotlinx.coroutines.flow.Flow

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
}