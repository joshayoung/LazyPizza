package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.mappers.toProductInCart
import com.joshayoung.lazypizza.core.domain.mappers.toToppingInCart
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.ProductInCart
import com.joshayoung.lazypizza.core.domain.models.ToppingInCart
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private var localDataSource: LocalDataSource,
    private var cartRemoteDataSource: CartRemoteDataSource
) : CartRepository {
    override suspend fun addProductToCart(product: Product): Long? {
        return localDataSource.addProductToCart(
            product.localId
        )
    }

    override suspend fun removeAllFromCart(lineNumber: Long) {
        localDataSource.removeAllFromCart(
            lineNumber
        )
    }

    override suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCart> {
        return localDataSource.getToppingForProductInCart(lineItemId).map { toppingInCartDto ->
            toppingInCartDto.toToppingInCart()
        }
    }

    override suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCart>> {
        return localDataSource.productsInCartWithNoToppings().map { productInCartDtoList ->
            productInCartDtoList.map { productInCartDto ->
                productInCartDto.toProductInCart()
            }
        }
    }

    override suspend fun productsInCartWithToppings(): Flow<List<ProductInCart>> {
        return localDataSource.productsInCartWithToppings().map { productInCartDtoList ->
            productInCartDtoList.map { productInCartDto ->
                productInCartDto.toProductInCart()
            }
        }
    }

    override suspend fun insertProductId(
        cartId: Long,
        productId: Long
    ): Long {
        return localDataSource.insertProductId(cartId, productId)
    }

    override suspend fun insertToppingId(
        lineItemNumber: Long,
        toppingId: Long,
        cartId: Long
    ) {
        localDataSource.insertToppingId(
            lineItemNumber = lineItemNumber,
            toppingId = toppingId,
            cartId = 1
        )
    }

    override suspend fun getProductInCart(lastLineNumber: Long): ProductsInCartEntity? {
        return localDataSource.getProductInCart(lastLineNumber)
    }

    override suspend fun deleteCartItem(item: ProductsInCartEntity) {
        localDataSource.deleteCartItem(item)
    }

    override suspend fun transferCartTo(
        owner: String?,
        user: String?
    ) {
        localDataSource.transferCart(owner, user)
    }

    override suspend fun clearCartForUser(user: String?) {
        localDataSource.clearCartForUser(user)
    }

    override suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    ) {
        if (localDataSource.doesCartExist(cartId)) {
            return
        }

        localDataSource.createCartForUser(cartId, theUser)
    }

    override fun allProductsWithCartItems(): Flow<List<ProductInCart>> {
        return localDataSource.allProductsWithCartItems().map { productInCartDtoList ->
            productInCartDtoList.map { productInCartDto ->
                productInCartDto.toProductInCart()
            }
        }
    }

    override fun getNumberProductsInCart(cartId: Long): Flow<Int> {
        return localDataSource.getNumberProductsInCart(cartId)
    }
}