package com.joshayoung.lazypizza.core.domain.models

import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity

data class InCartItem(
    val toppingsForDisplay: Map<String, Int>,
    val lineNumbers: List<Long?>,
    // TODO: Convert to domain layer model:
    val toppings: List<ToppingInCartEntity> = emptyList(),
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
