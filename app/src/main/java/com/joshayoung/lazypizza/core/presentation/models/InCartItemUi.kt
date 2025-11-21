package com.joshayoung.lazypizza.core.presentation.models

import com.joshayoung.lazypizza.core.data.network.dto.ToppingInCartDto
import com.joshayoung.lazypizza.menu.presentation.models.MenuTypeUi

data class InCartItemUi(
    val key: Int = 0,
    val toppingsForDisplay: Map<String, Int>,
    val lineNumbers: List<Long?>,
    // TODO: Convert to domain layer model:
    val toppings: List<ToppingInCartDto> = emptyList(),
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