package com.joshayoung.lazypizza.cart.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Ordered(
    val productRemoteId: String,
    val toppingRemoteIds: List<Long>
)
