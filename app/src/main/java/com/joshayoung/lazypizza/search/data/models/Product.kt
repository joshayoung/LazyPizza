package com.joshayoung.lazypizza.search.data.models

data class Product(
    val description: String,
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