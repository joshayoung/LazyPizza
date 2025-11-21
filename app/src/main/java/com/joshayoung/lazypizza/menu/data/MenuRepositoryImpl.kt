package com.joshayoung.lazypizza.menu.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toTopping
import com.joshayoung.lazypizza.core.presentation.mappers.toToppingEntity
import com.joshayoung.lazypizza.menu.domain.MenuRepository

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
}