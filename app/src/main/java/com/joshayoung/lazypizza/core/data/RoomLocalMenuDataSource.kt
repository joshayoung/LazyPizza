package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.core.data.database.ProductDao
import com.joshayoung.lazypizza.core.data.database.ToppingDao
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.domain.LocalMenuDataSource
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import kotlinx.coroutines.flow.Flow

class RoomLocalMenuDataSource(
    private var productDao: ProductDao,
    private var toppingDao: ToppingDao
) : LocalMenuDataSource {
    override suspend fun getAllToppings(): List<ToppingEntity> {
        return toppingDao.getAllToppings()
    }

    override suspend fun upsertTopping(toppingEntity: ToppingEntity) {
        toppingDao.upsertTopping(toppingEntity)
    }

    override suspend fun getAllProducts(): List<ProductEntity> {
        return productDao.getAllProducts()
    }

    override suspend fun getProduct(productId: String): Product {
        return productDao.getProduct(productId).toProduct()
    }

    override suspend fun sidesNotInCart(): Flow<List<ProductEntity>> {
        return productDao.sidesNotInCart()
    }

    override suspend fun upsertProduct(
        productEntity: ProductEntity
    ): Result<ProductEntity, DataError.Local> {
        productDao.upsertProduct(productEntity)

        // TODO: This return is probably not correct:
        return Result.Success(data = productEntity)
    }
}