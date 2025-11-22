package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import kotlinx.coroutines.flow.Flow

interface LocalMenuDataSource {
    suspend fun getProduct(productId: String): Product

    suspend fun getAllProducts(): List<ProductEntity>

    suspend fun sidesNotInCart(): Flow<List<ProductEntity>>

    suspend fun upsertProduct(productEntity: ProductEntity): Result<ProductEntity, DataError.Local>

    suspend fun getAllToppings(): List<ToppingEntity>

    suspend fun upsertTopping(toppingEntity: ToppingEntity)
}