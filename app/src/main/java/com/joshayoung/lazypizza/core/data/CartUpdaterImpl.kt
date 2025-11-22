package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.core.data.network.dto.ToppingInCartDto
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.CartUpdater
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi

// TODO: Could both 'insertProductWithToppings' be combined together?
class CartUpdaterImpl(
    private val cartRepository: CartRepository
) : CartUpdater {
    override suspend fun insertProductWithToppings(
        cartId: Long,
        productId: Long,
        toppings: List<ToppingInCartDto>
    ) {
        val lineItem =
            cartRepository.insertProductId(
                cartId = cartId,
                productId = productId
            )
        toppings.forEach { topping ->
            cartRepository.insertToppingId(
                lineItemNumber = lineItem,
                toppingId = topping.toppingId,
                cartId = 1
            )
        }
    }

    override suspend fun insertProductWithToppings(
        product: Product?,
        toppingsUi: List<ToppingUi>
    ) {
        val product = product
        if (product != null) {
            val lineItemNumber = cartRepository.addProductToCart(product)
            if (lineItemNumber == null) {
                return
            }
            toppingsUi.forEach { topping ->
                topping.localId?.let { id ->
                    cartRepository.insertToppingId(
                        lineItemNumber = lineItemNumber,
                        toppingId = id,
                        cartId = 1
                    )
                }
            }
        }
    }
}