package com.joshayoung.lazypizza.search.presentation.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductUi(
    val description: String? = null,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val name: String,
    val price: String,
    val type: ProductType? = null
) {
    val remoteImageUrl: String
        get() {
            return imageUrl ?: ""
        }
}
