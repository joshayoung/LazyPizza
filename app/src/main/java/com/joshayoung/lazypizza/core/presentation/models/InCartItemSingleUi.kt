package com.joshayoung.lazypizza.core.presentation.models

import com.joshayoung.lazypizza.core.data.network.dto.ToppingInCartDto
import com.joshayoung.lazypizza.menu.presentation.models.MenuTypeUi

data class InCartItemSingleUi(
    val toppings: List<ToppingInCartDto> = emptyList(),
    val productId: Long,
    var lineItemId: Long? = null,
    val remoteId: String,
    val name: String,
    val price: String,
    val nameWithToppingIds: String? = null,
    val toppingsForDisplay: Map<String, Int> = mapOf(),
    val description: String? = null,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val type: MenuTypeUi
)