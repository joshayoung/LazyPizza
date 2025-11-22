package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.core.data.network.models.ToppingInCartDto
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.CartUpdater
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.presentation.models.InCartItemSingleUi
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.core.presentation.utils.getMenuTypeEnum
import com.joshayoung.lazypizza.menu.data.mappers.toInCartItemUi
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import java.math.BigDecimal
import kotlin.collections.component1
import kotlin.collections.component2

// TODO: Could both 'insertProductWithToppings' be combined together?
class CartUpdaterImpl(
    private val cartRepository: CartRepository
) : CartUpdater {
    override suspend fun insertProductWithToppings(
        cartId: Long,
        productId: Long,
        toppings: List<ToppingInCartDto>
    ) {
        val lineItem =
            cartRepository.insertProductId(
                cartId = cartId,
                productId = productId
            )
        toppings.forEach { topping ->
            cartRepository.insertToppingId(
                lineItemNumber = lineItem,
                toppingId = topping.toppingId,
                cartId = 1
            )
        }
    }

    override suspend fun insertProductWithToppings(
        product: Product?,
        toppingsUi: List<ToppingUi>
    ) {
        val product = product
        if (product != null) {
            val lineItemNumber = cartRepository.addProductToCart(product)
            if (lineItemNumber == null) {
                return
            }
            toppingsUi.forEach { topping ->
                topping.localId?.let { id ->
                    cartRepository.insertToppingId(
                        lineItemNumber = lineItemNumber,
                        toppingId = id,
                        cartId = 1
                    )
                }
            }
        }
    }

    override suspend fun productsInCart(): Flow<List<InCartItemUi>> {
        var count = 0
        return cartRepository
            .productsInCartWithNoToppings()
            .combine(
                cartRepository
                    .productsInCartWithToppings()
            ) { productWithNoToppings, productWithToppings ->
                val groupedByProductId = productWithNoToppings.groupBy { it.productId }
                val inCartItemUis =
                    groupedByProductId.map { (_, productList) ->
                        productList.toInCartItemUi(key = ++count)
                    }

                val withToppings =
                    productWithToppings.map { iic ->
                        val toppings =
                            cartRepository.getToppingForProductInCart(
                                iic.lineItemId
                            )
                        val toppingsForDisplay =
                            toppings
                                .groupBy { it.name }
                                .mapValues { entry -> entry.value.size }
                        // could this be generic object?
                        InCartItemSingleUi(
                            name = iic.name,
                            description = iic.description,
                            imageResource = iic.imageResource,
                            toppingsForDisplay = toppingsForDisplay,
                            toppings = toppings,
                            lineItemId = iic.lineItemId,
                            imageUrl = iic.imageUrl,
                            nameWithToppingIds = iic.nameWithToppingIds,
                            type = getMenuTypeEnum(iic.type),
                            price = iic.price,
                            remoteId = iic.remoteId,
                            productId = iic.productId
                        )
                    }

                // group by something more unique:
                val grp = withToppings.groupBy { it.nameWithToppingIds }

                val inCartItemsWithToppingUis =
                    grp.map { item ->
                        val lineNumbers = item.value.map { it.lineItemId }
                        InCartItemUi(
                            key = ++count,
                            lineNumbers = lineNumbers,
                            name = item.value.first().name,
                            description = item.value.first().description,
                            imageResource = item.value.first().imageResource,
                            toppingsForDisplay = item.value.first().toppingsForDisplay,
                            toppings = item.value.first().toppings,
                            imageUrl = item.value.first().imageUrl,
                            type =
                                getMenuTypeEnum(
                                    item.value
                                        .first()
                                        .type.name
                                ),
                            price = item.value.first().price,
                            remoteId = item.value.first().remoteId,
                            productId = item.value.first().productId,
                            numberInCart = lineNumbers.count()
                        )
                    }

                inCartItemUis + inCartItemsWithToppingUis
            }.flowOn(Dispatchers.Default)
    }

    override fun getTotal(inCartItems: List<InCartItemUi>): BigDecimal {
        val base =
            inCartItems.sumOf { item ->
                item.price.toDouble() * item.numberInCart
            }
        val toppingsInCart = inCartItems.map { it.toppings }.flatMap { it }
        val toppings =
            toppingsInCart.sumOf { item ->
                item.price.toDouble()
            }

        return BigDecimal(base + toppings)
    }

    override suspend fun removeItemFromCart(lastLineNumber: Long?) {
        if (lastLineNumber != null) {
            val item = cartRepository.getProductInCart(lastLineNumber)
            if (item != null) {
                cartRepository.deleteCartItem(item)
            }
        }
    }

    override suspend fun removeAllFromCart(lineNumbers: List<Long?>) {
        lineNumbers.forEach { lineNumber ->
            if (lineNumber != null) {
                cartRepository.removeAllFromCart(lineNumber)
            }
        }
    }
}