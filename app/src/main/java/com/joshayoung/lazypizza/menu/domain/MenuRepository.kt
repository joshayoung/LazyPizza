package com.joshayoung.lazypizza.menu.domain

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    suspend fun getToppings(): List<Topping>

    suspend fun getProduct(productId: String): Product

    suspend fun updateLocalWithRemote(reload: Boolean = false)

    suspend fun updateLocalToppingsWithRemote(reload: Boolean = false)

    suspend fun sidesNotInCart(): Flow<List<Product>>
}