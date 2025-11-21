package com.joshayoung.lazypizza.order.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductWithToppings(
    val productRemoteId: String,
    val toppingRemoteIds: List<Long>
)