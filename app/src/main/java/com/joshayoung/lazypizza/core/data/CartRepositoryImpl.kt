package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.LocalCartDataSource
import com.joshayoung.lazypizza.core.domain.mappers.toProductInCart
import com.joshayoung.lazypizza.core.domain.mappers.toToppingInCart
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.ProductInCart
import com.joshayoung.lazypizza.core.domain.models.ToppingInCart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private var localCartDataSource: LocalCartDataSource
) : CartRepository {
    override suspend fun addProductToCart(product: Product): Long? {
        return localCartDataSource.addProductToCart(
            product.localId
        )
    }

    override suspend fun removeAllFromCart(lineNumber: Long) {
        localCartDataSource.removeAllFromCart(
            lineNumber
        )
    }

    override suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCart> {
        return localCartDataSource.getToppingForProductInCart(lineItemId).map { toppingInCartDto ->
            toppingInCartDto.toToppingInCart()
        }
    }

    override suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCart>> {
        return localCartDataSource.productsInCartWithNoToppings().map { productInCartDtoList ->
            productInCartDtoList.map { productInCartDto ->
                productInCartDto.toProductInCart()
            }
        }
    }

    override suspend fun productsInCartWithToppings(): Flow<List<ProductInCart>> {
        return localCartDataSource.productsInCartWithToppings().map { productInCartDtoList ->
            productInCartDtoList.map { productInCartDto ->
                productInCartDto.toProductInCart()
            }
        }
    }

    override suspend fun insertProductId(
        cartId: Long,
        productId: Long
    ): Long {
        return localCartDataSource.insertProductId(cartId, productId)
    }

    override suspend fun insertToppingId(
        lineItemNumber: Long,
        toppingId: Long,
        cartId: Long
    ) {
        localCartDataSource.insertToppingId(
            lineItemNumber = lineItemNumber,
            toppingId = toppingId,
            cartId = 1
        )
    }

    override suspend fun getProductInCart(lastLineNumber: Long): ProductsInCartEntity? {
        return localCartDataSource.getProductInCart(lastLineNumber)
    }

    override suspend fun deleteCartItem(item: ProductsInCartEntity) {
        localCartDataSource.deleteCartItem(item)
    }

    override suspend fun transferCartTo(
        owner: String?,
        user: String?
    ) {
        localCartDataSource.transferCart(owner, user)
    }

    override suspend fun clearCartForUser(user: String?) {
        localCartDataSource.clearCartForUser(user)
    }

    override suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    ) {
        if (localCartDataSource.doesCartExist(cartId)) {
            return
        }

        localCartDataSource.createCartForUser(cartId, theUser)
    }

    override fun allProductsWithCartItems(): Flow<List<ProductInCart>> {
        return localCartDataSource.allProductsWithCartItems().map { productInCartDtoList ->
            productInCartDtoList.map { productInCartDto ->
                productInCartDto.toProductInCart()
            }
        }
    }

    override fun getNumberProductsInCart(cartId: Long): Flow<Int> {
        return localCartDataSource.getNumberProductsInCart(cartId)
    }
}