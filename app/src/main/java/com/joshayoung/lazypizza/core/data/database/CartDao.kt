package com.joshayoung.lazypizza.core.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntityWithCartStatus
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCart
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCart
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
    suspend fun insertProductId(entry: ProductsInCart): Long

    @Insert
    suspend fun insertToppingId(entry: ToppingsInCart): Long

    @Query("SELECT * from toppings_in_cart where toppingId = :toppingId LIMIT 1")
    suspend fun getToppingItem(toppingId: Long): ToppingsInCart

    @Delete
    suspend fun deleteToppingFromCart(item: ToppingsInCart)

    @Query("DELETE FROM products_in_cart WHERE productId = :productId")
    suspend fun deleteAll(productId: Long)

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM topping")
    suspend fun getAllToppings(): List<ToppingEntity>

    @Query("SELECT * FROM product where remoteId = :productId")
    suspend fun getProduct(productId: String): ProductEntity

    @Delete
    suspend fun deleteCartItem(item: ProductsInCart)

    @Query(
        "SELECT products_in_cart.id, products_in_cart.cartPivotId, products_in_cart.productId FROM products_in_cart join product on product.productId = products_in_cart.productId where product.remoteId = :productId LIMIT 1"
    )
    suspend fun getProductInCart(productId: String): ProductsInCart?

    @Query("SELECT productId from products_in_cart where cartPivotId = :cartPivotId")
    suspend fun getCartProducts(cartPivotId: Long): List<Long>

    @Query("SELECT COUNT(productId) from products_in_cart where cartPivotId = :cartPivotId")
    fun getNumberProductsInCart(cartPivotId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM cart WHERE cartId = :cartId")
    suspend fun doesCartExist(cartId: Long): Boolean

    @Query(
        "select pivot.id as lineItemId, p.productId, p.remoteId, p.name, p.price, p.description, p.imageUrl, p.imageResource, p.type, COUNT(pivot.productId) as numberInCart from product as p   left  join products_in_cart as pivot on pivot.productId == p.productId group by p.remoteId"
    )
    suspend fun allProductsWithCartItems(): List<ProductEntityWithCartStatus>

    @Query(
        "select pivot.id as lineItemId, p.productId, p.remoteId, p.name, p.price, p.description, p.imageUrl, p.imageResource, p.type, COUNT(pivot.productId) as numberInCart from product as p join products_in_cart as pivot on pivot.productId == p.productId group by p.remoteId"
    )
    suspend fun productsInCart(): List<ProductEntityWithCartStatus>
}