package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.CartProductId
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductEntity
import com.joshayoung.lazypizza.core.utils.toFlowList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private var roomLocalDataSource: RoomLocalDataSource,
    private var cartRemoteDataSource: CartRemoteDataSource
) : CartRepository {
    override suspend fun addProductToCart(product: Product) {
        roomLocalDataSource.addProductToCart(
            product.localId
        )
    }

    override fun getCart(): Flow<CartEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun removeProductFromCart(product: Product) {
        TODO("Not yet implemented")
    }

    // TODO: Add a background sync to update local cache:
    override suspend fun getProducts(): Flow<List<Product>> {
        val localProducts = roomLocalDataSource.getAllProducts()
        if (!localProducts.isEmpty()) {
            return localProducts.map { it.toProduct() }.toFlowList()
        } else {
            val remoteProducts =
                cartRemoteDataSource
                    .getProducts(
                        BuildConfig.MENU_ITEMS_COLLECTION_ID
                    )

            remoteProducts.forEach { product ->
                roomLocalDataSource.upsertProduct(product.toProductEntity())
            }

            return remoteProducts.toFlowList()
        }
    }
}