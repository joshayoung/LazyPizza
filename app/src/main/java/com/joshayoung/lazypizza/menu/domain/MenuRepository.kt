package com.joshayoung.lazypizza.menu.domain

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping

interface MenuRepository {
    suspend fun getToppings(): List<Topping>

    suspend fun getProduct(productId: String): Product
}