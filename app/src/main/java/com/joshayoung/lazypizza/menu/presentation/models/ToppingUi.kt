package com.joshayoung.lazypizza.menu.presentation.models

import java.math.BigDecimal

data class ToppingUi(
    val localId: Long? = null,
    val remoteId: String,
    val name: String,
    val price: BigDecimal,
    val imageResource: Int? = null,
    val imageUrl: String? = null
)
