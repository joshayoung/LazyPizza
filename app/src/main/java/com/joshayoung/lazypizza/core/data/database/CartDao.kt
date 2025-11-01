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
import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCart
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCart(cart: CartEntity)

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

    @Delete
    suspend fun deleteCartItem(item: ProductsInCart)

    @Query(
        "SELECT products_in_cart.id, products_in_cart.cartId, products_in_cart.productId FROM products_in_cart join product on product.productId = products_in_cart.productId where products_in_cart.id = :lineItemId"
    )
    suspend fun getProductInCart(lineItemId: Long): ProductsInCart?

    @Query("SELECT productId from products_in_cart where cartId = :cartId")
    suspend fun getCartProducts(cartId: Long): List<Long>

    @Query("SELECT COUNT(productId) from products_in_cart where cartId = :cartPivotId")
    fun getNumberProductsInCart(cartPivotId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM cart WHERE cartId = :cartId")
    suspend fun doesCartExist(cartId: Long): Boolean

    @Query(
        "select pivot.id as lineItemId, p.productId, p.remoteId, p.name, p.price, p.description, p.imageUrl, p.imageResource, p.type from product as p left join products_in_cart as pivot on pivot.productId == p.productId "
    )
    fun allProductsWithCartItems(): Flow<List<ProductWithCartStatusEntity>>

    @Query(
        "select pivot.id as lineItemId, p.productId, p.remoteId, p.name, p.price, p.description, p.imageUrl, p.imageResource, p.type from product as p join products_in_cart as pivot on pivot.productId == p.productId where pivot.id NOT IN (select lineItemNumber from toppings_in_cart)"
    )
    fun productsInCartWithNoToppings(): Flow<List<ProductInCartEntity>>

    @Query(
        "select pivot.id as lineItemId, p.productId, p.remoteId, p.price, p.description, p.imageUrl, p.imageResource, p.type, p.name, GROUP_CONCAT(t.name, ', ') as toppingList from product as p join products_in_cart as pivot on pivot.productId == p.productId join toppings_in_cart as tic on tic.lineItemNumber = pivot.id join topping as t on t.toppingId = tic.toppingId where pivot.id IN (select lineItemNumber from toppings_in_cart) group by pivot.id"
    )
    fun productsInCartWithToppings(): Flow<List<ProductInCartEntity>>

    @Query(
        "select topping.toppingId, topping.remoteId, topping.name, topping.price, topping.imageUrl, products_in_cart.productId, count(toppings_in_cart.toppingId) as numberOfToppings from toppings_in_cart join topping on topping.toppingId = toppings_in_cart.toppingId join products_in_cart on toppings_in_cart.lineItemNumber = products_in_cart.id where toppings_in_cart.lineItemNumber = :lineItemNumber group by toppings_in_cart.toppingId"
    )
    suspend fun getToppingsForProductInCart(lineItemNumber: Long): List<ToppingInCartEntity>

    @Query(
        "select topping.toppingId, topping.remoteId, topping.name, topping.price, topping.imageUrl, products_in_cart.productId from toppings_in_cart join topping on topping.toppingId = toppings_in_cart.toppingId join products_in_cart on toppings_in_cart.lineItemNumber = products_in_cart.id where products_in_cart.id = :lineItemNumber"
    )
    suspend fun getToppingForProductInCart(lineItemNumber: Long?): List<ToppingInCartEntity>

    @Query("DELETE from products_in_cart where id = :lineNumber")
    suspend fun deleteItemFromCart(lineNumber: Long)
}