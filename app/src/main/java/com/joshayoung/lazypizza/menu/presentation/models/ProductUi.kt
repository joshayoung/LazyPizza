package com.joshayoung.lazypizza.menu.presentation.models

import com.joshayoung.lazypizza.core.data.network.models.ToppingInCartDto
import java.math.BigDecimal

data class ProductUi(
    val id: String,
    val localId: Long?,
    val description: String? = null,
    val remoteImageUrl: String? = null,
    val imageResource: Int? = null,
    val name: String,
    val productId: Long? = null,
    val lineItemId: Long? = null,
    val price: BigDecimal,
    val type: MenuTypeUi? = null,
    val inCart: Boolean = false,
    val numberInCart: Int? = null,
    val totalPrice: BigDecimal = BigDecimal(0.0),
    val toppingTotal: BigDecimal = BigDecimal(0.0),
    val toppings: List<ToppingInCartDto>? = null
)
