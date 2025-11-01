package com.joshayoung.lazypizza.core.presentation.models

import com.joshayoung.lazypizza.core.data.database.dto.ToppingInCartDto

data class InCartItemUi(
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
    val type: String,
    val numberInCart: Int = 0
)