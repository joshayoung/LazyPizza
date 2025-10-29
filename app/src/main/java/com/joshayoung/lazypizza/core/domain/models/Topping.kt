package com.joshayoung.lazypizza.core.domain.models

data class Topping(
    val localId: Long? = null,
    val remoteId: String,
    val name: String,
    val price: String,
    val imageUrl: String? = null
)
