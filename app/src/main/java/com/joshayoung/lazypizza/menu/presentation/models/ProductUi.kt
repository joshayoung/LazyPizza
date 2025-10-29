package com.joshayoung.lazypizza.menu.presentation.models

import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity
import com.joshayoung.lazypizza.core.domain.serializers.BigDecimalSerializer
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class ProductUi(
    val id: String,
    val localId: Long?,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageResource: Int? = null,
    val name: String,
    val productId: Long? = null,
    val lineItemId: Long? = null,
    @Serializable(with = BigDecimalSerializer::class) val price: BigDecimal,
    val type: MenuType? = null,
    val inCart: Boolean = false,
    val numberInCart: Int = 0,
    @Serializable(with = BigDecimalSerializer::class) val totalPrice: BigDecimal =
        BigDecimal(0.0),
    @Serializable(with = BigDecimalSerializer::class) val toppingTotal: BigDecimal =
        BigDecimal(0.0),
    val toppings: List<ToppingInCartEntity>? = null
) {
    val remoteImageUrl: String
        get() {
            return imageUrl ?: ""
        }
}
