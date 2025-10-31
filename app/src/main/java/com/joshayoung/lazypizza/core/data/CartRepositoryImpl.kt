package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductWithCartStatusEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCart
import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCart
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductEntity
import com.joshayoung.lazypizza.core.presentation.mappers.toTopping
import com.joshayoung.lazypizza.core.presentation.mappers.toToppingEntity
import com.joshayoung.lazypizza.core.utils.toFlowList
import kotlinx.coroutines.flow.Flow

class CartRepositoryImpl(
    private var localDataSource: LocalDataSource,
    private var cartRemoteDataSource: CartRemoteDataSource
) : CartRepository {
    override suspend fun addProductToCart(product: Product): Long? {
        return localDataSource.addProductToCart(
            product.localId
        )
    }

    override fun getCart(): Flow<CartEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun removeAllFromCart(lineNumber: Long) {
        localDataSource.removeAllFromCart(
            lineNumber
        )
    }

    // TODO: Add a background sync to update local cache:
    override suspend fun getProducts(): Flow<List<Product>> {
        val localProducts = localDataSource.getAllProducts()
        if (!localProducts.isEmpty()) {
            return localProducts.map { it.toProduct() }.toFlowList()
        } else {
            val remoteProducts =
                cartRemoteDataSource
                    .getProducts(
                        BuildConfig.MENU_ITEMS_COLLECTION_ID
                    )

            remoteProducts.forEach { product ->
                localDataSource.upsertProduct(product.toProductEntity())
            }

            return remoteProducts.toFlowList()
        }
    }

    override suspend fun getToppings(): List<Topping> {
        val localToppings = localDataSource.getAllToppings()
        if (!localToppings.isEmpty()) {
            return localToppings.map { it.toTopping() }
        } else {
            val remoteToppings =
                cartRemoteDataSource
                    .getToppings(
                        BuildConfig.TOPPINGS_COLLECTION_ID
                    )

            remoteToppings.forEach { topping ->
                localDataSource.upsertTopping(topping.toToppingEntity())
            }

            return remoteToppings
        }
    }

    override suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCartEntity> {
        return localDataSource.getToppingForProductInCart(lineItemId)
    }

    override suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCartEntity>> {
        return localDataSource.productsInCartWithNoToppings()
    }

    override suspend fun productsInCartWithToppings(): Flow<List<ProductInCartEntity>> {
        return localDataSource.productsInCartWithToppings()
    }

    override suspend fun insertProductId(productsInCart: ProductsInCart): Long {
        return localDataSource.insertProductId(productsInCart)
    }

    override suspend fun updateLocalWithRemote(reload: Boolean) {
        val localProducts = localDataSource.getAllProducts()
        if (!localProducts.isEmpty() && !reload) {
            return
        }

        val remoteProducts = cartRemoteDataSource.getProducts(BuildConfig.MENU_ITEMS_COLLECTION_ID)
        remoteProducts.forEach { product ->
            localDataSource.upsertProduct(product.toProductEntity())
        }
    }

    override suspend fun updateLocalToppingsWithRemote(reload: Boolean) {
        val localToppings = localDataSource.getAllToppings()
        if (!localToppings.isEmpty() && !reload) {
            return
        }

        val remoteToppings = cartRemoteDataSource.getToppings(BuildConfig.TOPPINGS_COLLECTION_ID)
        remoteToppings.forEach { topping ->
            localDataSource.upsertTopping(topping.toToppingEntity())
        }
    }

    override suspend fun insertToppingId(toppingsInCart: ToppingsInCart) {
        localDataSource.insertToppingId(toppingsInCart)
    }

    override suspend fun removeProductFromCart(product: Product) {
//        val lineItems = cartDao.
        localDataSource.removeProductFromCart(product)
    }

    override suspend fun getProductInCart(lastLineNumber: Long): ProductsInCart? {
        return localDataSource.getProductInCart(lastLineNumber)
    }

    override suspend fun getAllProducts(): List<Product> =
        localDataSource.getAllProducts().map {
            it.toProduct()
        }

    override suspend fun deleteCartItem(item: ProductsInCart) {
        localDataSource.deleteCartItem(item)
    }

    override suspend fun sidesNotInCart(): Flow<List<ProductEntity>> {
        return localDataSource.sidesNotInCart()
    }

    override suspend fun createCartForUser(
        cartId: Long,
        theUser: String
    ) {
        if (localDataSource.doesCartExist(cartId)) {
            return
        }

        localDataSource.createCartForUser(cartId, theUser)
    }

    override fun allProductsWithCartItems(): Flow<List<ProductWithCartStatusEntity>> {
        return localDataSource.allProductsWithCartItems()
    }

    override suspend fun getNumberProductsInCart(cartId: Long): Flow<Int> {
        return localDataSource.getNumberProductsInCart(cartId)
    }

    override suspend fun getProduct(productId: String): Product {
        return localDataSource.getProduct(productId)
    }
}