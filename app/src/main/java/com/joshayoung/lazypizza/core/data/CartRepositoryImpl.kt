package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.ProductEntityWithCartStatus
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductEntity
import com.joshayoung.lazypizza.core.utils.toFlowList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private var localDataSource: LocalDataSource,
    private var cartRemoteDataSource: CartRemoteDataSource
) : CartRepository {
    override suspend fun addProductToCart(product: Product) {
        localDataSource.addProductToCart(
            product.localId
        )
    }

    override fun getCart(): Flow<CartEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun removeAllFromCart(product: Product) {
        localDataSource.removeAllFromCart(
            product.localId
        )
    }

    // TODO: Add a background sync to update local cache:
    override suspend fun getProducts(): Flow<List<Product>> {
        val localProducts = localDataSource.getAllProducts()
        if (!localProducts.isEmpty()) {
            return localProducts.map { it.toProduct() }.toFlowList()
        } else {
            val remoteProducts =
                cartRemoteDataSource
                    .getProducts(
                        BuildConfig.MENU_ITEMS_COLLECTION_ID
                    )

            remoteProducts.forEach { product ->
                localDataSource.upsertProduct(product.toProductEntity())
            }

            return remoteProducts.toFlowList()
        }
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

    override suspend fun removeProductFromCart(product: Product) {
        localDataSource.removeProductFromCart(product)
    }

    override suspend fun getAllProducts(): List<Product> =
        localDataSource.getAllProducts().map {
            it.toProduct()
        }

    override suspend fun createCartForUser(cartId: Long) {
        if (localDataSource.doesCartExist(cartId)) {
            return
        }

        localDataSource.createCartForUser(cartId)
    }

    override suspend fun allProductsWithCartItems(): List<ProductEntityWithCartStatus> =
        localDataSource.allProductsWithCartItems()

    override suspend fun getNumberProductsInCart(cartId: Long): Flow<Int> {
        return localDataSource.getNumberProductsInCart(cartId)
    }

    override suspend fun productsInCart(): List<ProductEntityWithCartStatus> {
        return localDataSource.productsInCart()
    }
}