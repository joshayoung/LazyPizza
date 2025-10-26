package com.joshayoung.lazypizza.core.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.CartProductId
import com.joshayoung.lazypizza.core.domain.models.ProductEntity
import com.joshayoung.lazypizza.core.domain.models.ProductEntityWithCartStatus
import com.joshayoung.lazypizza.core.domain.models.ProductToppings
import com.joshayoung.lazypizza.core.domain.models.ToppingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCart(cart: CartEntity)

    @Upsert
    suspend fun upsertProduct(productEntity: ProductEntity)

    @Upsert
    suspend fun upsertTopping(toppingEntity: ToppingEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProductId(entry: CartProductId): Long

    @Insert
    suspend fun insertToppingId(entry: ProductToppings): Long

    @Query("SELECT * from product_toppings where toppingId = :toppingId LIMIT 1")
    suspend fun getToppingItem(toppingId: Long): ProductToppings

    @Delete
    suspend fun deleteToppingFromCart(item: ProductToppings)

    @Query("DELETE FROM cart_product_ids WHERE productId = :productId")
    suspend fun deleteAll(productId: Long)

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM topping")
    suspend fun getAllToppings(): List<ToppingEntity>

    @Query("SELECT * FROM product where remoteId = :productId")
    suspend fun getProduct(productId: String): ProductEntity

    @Delete
    suspend fun deleteCartItem(item: CartProductId)

    @Query(
        "SELECT cart_product_ids.id, cart_product_ids.cartPivotId, cart_product_ids.productId FROM cart_product_ids join product on product.productId = cart_product_ids.productId where product.remoteId = :productId LIMIT 1"
    )
    suspend fun getProductInCart(productId: String): CartProductId?

    @Query("SELECT productId from cart_product_ids where cartPivotId = :cartPivotId")
    suspend fun getCartProducts(cartPivotId: Long): List<Long>

    @Query("SELECT COUNT(productId) from cart_product_ids where cartPivotId = :cartPivotId")
    fun getNumberProductsInCart(cartPivotId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM cart WHERE cartId = :cartId")
    suspend fun doesCartExist(cartId: Long): Boolean

    @Query(
        "select pivot.id as lineItemId, p.productId, p.remoteId, p.name, p.price, p.description, p.imageUrl, p.imageResource, p.type, COUNT(pivot.productId) as numberInCart from product as p   left  join cart_product_ids as pivot on pivot.productId == p.productId group by p.remoteId"
    )
    suspend fun allProductsWithCartItems(): List<ProductEntityWithCartStatus>

    @Query(
        "select pivot.id as lineItemId, p.productId, p.remoteId, p.name, p.price, p.description, p.imageUrl, p.imageResource, p.type, COUNT(pivot.productId) as numberInCart from product as p join cart_product_ids as pivot on pivot.productId == p.productId group by p.remoteId"
    )
    suspend fun productsInCart(): List<ProductEntityWithCartStatus>
}