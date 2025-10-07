package com.joshayoung.lazypizza.search.data.models

data class Product(
    val description: String,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val name: String,
    val price: String
)