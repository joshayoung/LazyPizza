package com.joshayoung.lazypizza.core.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductWithCartStatusEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCart
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity
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
        "SELECT products_in_cart.id, products_in_cart.cartId, products_in_cart.productId FROM products_in_cart join product on product.productId = products_in_cart.productId where product.remoteId = :productId LIMIT 1"
    )
    suspend fun getProductInCart(productId: String): ProductsInCart?

    @Query("SELECT productId from products_in_cart where cartId = :cartId")
    suspend fun getCartProducts(cartId: Long): List<Long>

    @Query("SELECT COUNT(productId) from products_in_cart where cartId = :cartPivotId")
    fun getNumberProductsInCart(cartPivotId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM cart WHERE cartId = :cartId")
    suspend fun doesCartExist(cartId: Long): Boolean

    @Query(
        "select * from product where productId not in (select productId from products_in_cart) and type != 'entree'"
    )
    suspend fun sidesNotInCart(): List<ProductEntity>

    @Query(
        "select pivot.id as lineItemId, p.productId, p.remoteId, p.name, p.price, p.description, p.imageUrl, p.imageResource, p.type, COUNT(pivot.productId) as numberInCart from product as p   left  join products_in_cart as pivot on pivot.productId == p.productId group by p.remoteId"
    )
    suspend fun allProductsWithCartItems(): List<ProductWithCartStatusEntity>

    @Query(
        "select pivot.id as lineItemId, p.productId, p.remoteId, p.name, p.price, p.description, p.imageUrl, p.imageResource, p.type, COUNT(pivot.productId) as numberInCart from product as p join products_in_cart as pivot on pivot.productId == p.productId group by p.remoteId"
    )
    fun productsInCart(): Flow<List<ProductWithCartStatusEntity>>

    @Query(
        "select p.productId, p.remoteId, p.name, p.price, p.description, p.imageUrl, p.imageResource, p.type, COUNT(pivot.cartId) as numberInCart from product as p join products_in_cart as pivot on pivot.productId == p.productId where pivot.id NOT IN (select lineItemNumber from toppings_in_cart) group by p.productId order by type desc"
    )
    fun productsInCartWithNoToppings(): Flow<List<ProductInCartEntity>>

    @Query(
        "select pivot.id as lineItemId, p.productId, p.remoteId, p.name, p.price, p.description, p.imageUrl, p.imageResource, p.type from product as p join products_in_cart as pivot on pivot.productId == p.productId where pivot.id IN (select lineItemNumber from toppings_in_cart)"
    )
    fun productsInCartWithToppings(): Flow<List<ProductInCartEntity>>

    @Query(
        "select topping.remoteId, topping.name, topping.price, topping.imageUrl, products_in_cart.productId, count(toppings_in_cart.toppingId) as numberOfToppings from toppings_in_cart join topping on topping.toppingId = toppings_in_cart.toppingId join products_in_cart on toppings_in_cart.lineItemNumber = products_in_cart.id where toppings_in_cart.lineItemNumber = :lineItemNumber group by toppings_in_cart.toppingId"
    )
    suspend fun getToppingsForProductInCart(lineItemNumber: Long): List<ToppingInCartEntity>
}