package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductWithCartStatusEntity
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductEntity
import com.joshayoung.lazypizza.core.presentation.mappers.toTopping
import com.joshayoung.lazypizza.core.presentation.mappers.toToppingEntity
import com.joshayoung.lazypizza.core.utils.toFlowList
import kotlinx.coroutines.flow.Flow

class CartRepositoryImpl(
    private var localDataSource: LocalDataSource,
    private var cartRemoteDataSource: CartRemoteDataSource,
    private var cartDao: CartDao
) : CartRepository {
    override suspend fun addProductToCart(product: Product): Long? {
        return localDataSource.addProductToCart(
            product.localId
        )
    }

    override fun getCart(): Flow<CartEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun removeAllFromCart(lineNumber: Long) {
        localDataSource.removeAllFromCart(
            lineNumber
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

    override suspend fun getToppings(): Flow<List<Topping>> {
        val localToppings = localDataSource.getAllToppings()
        if (!localToppings.isEmpty()) {
            return localToppings.map { it.toTopping() }.toFlowList()
        } else {
            val remoteToppings =
                cartRemoteDataSource
                    .getToppings(
                        BuildConfig.TOPPINGS_COLLECTION_ID
                    )

            remoteToppings.forEach { topping ->
                localDataSource.upsertTopping(topping.toToppingEntity())
            }

            return remoteToppings.toFlowList()
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

    override suspend fun removeProductFromCart(product: Product) {
//        val lineItems = cartDao.
        localDataSource.removeProductFromCart(product)
    }

    override suspend fun getAllProducts(): List<Product> =
        localDataSource.getAllProducts().map {
            it.toProduct()
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

    override fun allProductsWithCartItems(): Flow<List<ProductWithCartStatusEntity>> {
        return localDataSource.allProductsWithCartItems()
    }

    override suspend fun getNumberProductsInCart(cartId: Long): Flow<Int> {
        return localDataSource.getNumberProductsInCart(cartId)
    }

    override suspend fun getProduct(productId: String): Product {
        return localDataSource.getProduct(productId)
    }
}