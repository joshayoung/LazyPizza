package com.joshayoung.lazypizza.core.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Upsert
    suspend fun upsertProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM product where remoteId = :remoteId")
    suspend fun getProduct(remoteId: String): ProductEntity

    @Query(
        "select * from product where productId not in (select productId from products_in_cart) and type != 'entree'"
    )
    fun sidesNotInCart(): Flow<List<ProductEntity>>
}