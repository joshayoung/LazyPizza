package com.joshayoung.lazypizza.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val description: String? = null,
    val imageUrl: String? = null,
    val plImageUrl: String? = null,
    val imageResource: Int? = null,
    val name: String,
    val price: String
) {
    val remoteImageUrl: String
        get() {
            return imageUrl ?: ""
//            return plImageUrl ?: ""
        }
}