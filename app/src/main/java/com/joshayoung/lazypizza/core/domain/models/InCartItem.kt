package com.joshayoung.lazypizza.core.domain.models

import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity

data class InCartItem(
    val name: String,
    val productId: Long,
    val price: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val toppingsForDisplay: Map<String, Int>,
    val type: String,
    val numberInCart: Int = 0,
    val lineNumbers: List<Long>,
    // TODO: Convert to domain layer model:
    val toppings: List<ToppingInCartEntity> = emptyList()
)
