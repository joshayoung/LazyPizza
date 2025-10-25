package com.joshayoung.lazypizza.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val lineItemId: Long? = null,
    val localId: Long? = null,
    val name: String,
    val price: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val type: String
)