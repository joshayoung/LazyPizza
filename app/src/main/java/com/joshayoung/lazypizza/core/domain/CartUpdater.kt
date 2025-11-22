package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.data.network.dto.ToppingInCartDto
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi

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
}