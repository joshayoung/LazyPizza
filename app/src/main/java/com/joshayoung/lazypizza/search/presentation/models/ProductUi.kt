package com.joshayoung.lazypizza.search.presentation.models

import com.joshayoung.lazypizza.core.domain.serializers.BigDecimalSerializer
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
    val type: ProductType? = null
) {
    val remoteImageUrl: String
        get() {
            return imageUrl ?: ""
        }
}
