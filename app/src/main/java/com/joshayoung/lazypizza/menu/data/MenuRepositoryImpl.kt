package com.joshayoung.lazypizza.menu.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductEntity
import com.joshayoung.lazypizza.core.presentation.mappers.toTopping
import com.joshayoung.lazypizza.core.presentation.mappers.toToppingEntity
import com.joshayoung.lazypizza.menu.domain.MenuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MenuRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val cartRemoteDataSource: CartRemoteDataSource
) : MenuRepository {
    override suspend fun getProduct(productId: String): Product {
        return localDataSource.getProduct(productId)
    }

    override suspend fun getToppings(): List<Topping> {
        val localToppings = localDataSource.getAllToppings()
        if (!localToppings.isEmpty()) {
            return localToppings.map { it.toTopping() }
        } else {
            val remoteToppings =
                cartRemoteDataSource
                    .getToppings(
                        BuildConfig.TOPPINGS_COLLECTION_ID
                    )

            remoteToppings.forEach { topping ->
                localDataSource.upsertTopping(topping.toToppingEntity())
            }

            return remoteToppings
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

    override suspend fun sidesNotInCart(): Flow<List<Product>> {
        return localDataSource.sidesNotInCart().map {
            it.map { item ->
                item.toProduct()
            }
        }
    }
}