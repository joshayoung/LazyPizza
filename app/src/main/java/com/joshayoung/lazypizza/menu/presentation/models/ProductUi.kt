package com.joshayoung.lazypizza.menu.presentation.models

import com.joshayoung.lazypizza.core.domain.serializers.BigDecimalSerializer
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class ProductUi(
    val id: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val name: String,
    @Serializable(with = BigDecimalSerializer::class) val price: BigDecimal,
    val type: MenuType? = null
) {
    val remoteImageUrl: String
        get() {
            return imageUrl ?: ""
        }
}
