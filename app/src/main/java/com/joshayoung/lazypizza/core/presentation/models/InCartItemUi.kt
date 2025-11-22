package com.joshayoung.lazypizza.core.presentation.models

import com.joshayoung.lazypizza.core.domain.models.ToppingInCart
import com.joshayoung.lazypizza.menu.presentation.models.MenuTypeUi

data class InCartItemUi(
    val key: Int = 0,
    val toppingsForDisplay: Map<String, Int>,
    val lineNumbers: List<Long?>,
    val toppings: List<ToppingInCart> = emptyList(),
    val productId: Long,
    val remoteId: String,
    val name: String,
    val price: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val type: MenuTypeUi,
    val numberInCart: Int = 0
)