package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.network.dto.ProductInCartDto
import com.joshayoung.lazypizza.core.data.network.dto.ToppingInCartDto
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductEntity
import com.joshayoung.lazypizza.core.presentation.mappers.toToppingEntity
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

    override suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCartDto> {
        return localDataSource.getToppingForProductInCart(lineItemId)
    }

    override suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCartDto>> {
        return localDataSource.productsInCartWithNoToppings()
    }

    override suspend fun productsInCartWithToppings(): Flow<List<ProductInCartDto>> {
        return localDataSource.productsInCartWithToppings()
    }

    override suspend fun insertProductId(
        cartId: Long,
        productId: Long
    ): Long {
        return localDataSource.insertProductId(cartId, productId)
    }

    override suspend fun updateLocalWithRemote(reload: Boolean) {
        val localProducts = localDataSource.getAllProducts()
        if (!localProducts.isEmpty() && !reload) {
            return
        }

        val remoteProducts = cartRemoteDataSource.getProducts(BuildConfig.MENU_ITEMS_COLLECTION_ID)
        remoteProducts.forEach { product ->
            localDataSource.upsertProduct(product.toProductEntity())
        }
    }

    override suspend fun updateLocalToppingsWithRemote(reload: Boolean) {
        val localToppings = localDataSource.getAllToppings()
        if (!localToppings.isEmpty() && !reload) {
            return
        }

        val remoteToppings = cartRemoteDataSource.getToppings(BuildConfig.TOPPINGS_COLLECTION_ID)
        remoteToppings.forEach { topping ->
            localDataSource.upsertTopping(topping.toToppingEntity())
        }
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

    override suspend fun sidesNotInCart(): Flow<List<Product>> {
        return localDataSource.sidesNotInCart().map {
            it.map { item ->
                item.toProduct()
            }
        }
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

    override fun allProductsWithCartItems(): Flow<List<ProductInCartDto>> {
        return localDataSource.allProductsWithCartItems()
    }

    override fun getNumberProductsInCart(cartId: Long): Flow<Int> {
        return localDataSource.getNumberProductsInCart(cartId)
    }
}