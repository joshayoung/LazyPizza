package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.data.database.dto.ProductInCartDto
import com.joshayoung.lazypizza.core.data.database.dto.ProductWithCartStatusDto
import com.joshayoung.lazypizza.core.data.database.dto.ToppingInCartDto
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCartEntity
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.LocalDataSource
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toProductEntity
import com.joshayoung.lazypizza.core.presentation.mappers.toTopping
import com.joshayoung.lazypizza.core.presentation.mappers.toToppingEntity
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

    override suspend fun removeAllFromCart(lineNumber: Long) {
        localDataSource.removeAllFromCart(
            lineNumber
        )
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

    override suspend fun getToppingForProductInCart(lineItemId: Long?): List<ToppingInCartDto> {
        return localDataSource.getToppingForProductInCart(lineItemId)
    }

    override suspend fun productsInCartWithNoToppings(): Flow<List<ProductInCartDto>> {
        return localDataSource.productsInCartWithNoToppings()
    }

    override suspend fun productsInCartWithToppings(): Flow<List<ProductInCartDto>> {
        return localDataSource.productsInCartWithToppings()
    }

    override suspend fun insertProductId(productsInCartEntity: ProductsInCartEntity): Long {
        return localDataSource.insertProductId(productsInCartEntity)
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

    override suspend fun insertToppingId(toppingsInCartEntity: ToppingsInCartEntity) {
        localDataSource.insertToppingId(toppingsInCartEntity)
    }

    override suspend fun getProductInCart(lastLineNumber: Long): ProductsInCartEntity? {
        return localDataSource.getProductInCart(lastLineNumber)
    }

    override suspend fun deleteCartItem(item: ProductsInCartEntity) {
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

    override fun allProductsWithCartItems(): Flow<List<ProductWithCartStatusDto>> {
        return localDataSource.allProductsWithCartItems()
    }

    override fun getNumberProductsInCart(cartId: Long): Flow<Int> {
        return localDataSource.getNumberProductsInCart(cartId)
    }

    override suspend fun getProduct(productId: String): Product {
        return localDataSource.getProduct(productId)
    }
}