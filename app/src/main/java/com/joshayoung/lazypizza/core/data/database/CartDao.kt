package com.joshayoung.lazypizza.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.CartProductId
import com.joshayoung.lazypizza.core.domain.models.CartWithProducts
import com.joshayoung.lazypizza.core.domain.models.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCart(cart: CartEntity)

    @Upsert
    suspend fun upsertProduct(productEntity: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProductId(entry: CartProductId): Long

    @Query("DELETE FROM cart_product_ids WHERE productId = :productId")
    suspend fun deleteProductId(productId: Long)

    @Query("SELECT * FROM product")
    fun getProduct(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM product WHERE productId = :productId LIMIT 1")
    suspend fun getProductById(productId: Long): ProductEntity

    @Transaction
    @Query("SELECT * FROM cart")
    suspend fun getAllNotesByCategory(): List<CartWithProducts>

    @Query(
        "SELECT COUNT(*) FROM cart_product_ids WHERE cartPivotId = :cartId AND productId = :productId"
    )
    suspend fun exists(
        cartId: Long,
        productId: Long
    ): Int

    @Query("SELECT productId from cart_product_ids where cartPivotId = :cartPivotId")
    suspend fun getCartProducts(cartPivotId: Long): List<Long>

    @Query("SELECT COUNT(*) FROM cart WHERE cartId = :cartId")
    suspend fun doesCartExist(cartId: Long): Boolean
}