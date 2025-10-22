package com.joshayoung.lazypizza.cart.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.cart.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.models.Product
import io.appwrite.Client
import io.appwrite.services.TablesDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlin.collections.map

private object CartValues {
    val CART_ITEMS = stringPreferencesKey("cart_items")
}

class DataStorageCartRepository(
    private var appWriteClient: Client,
    private val dataStore: DataStore<Preferences>
) : CartRepository {
    override suspend fun addToCart(product: Product) {
        val productsString =
            dataStore.data
                .map { preferences -> preferences[CartValues.CART_ITEMS] }
                .firstOrNull()
        if (productsString != null) {
            val products = Json.decodeFromString<List<Product>>(productsString).toMutableList()
            products.add(product)
            val jsonProducts = Json.encodeToString(products)

            dataStore.edit { preferences ->
                preferences[CartValues.CART_ITEMS] = jsonProducts
            }
        } else {
            val products =
                listOf(
                    product
                )
            val jsonProducts = Json.encodeToString(products)

            dataStore.edit { preferences ->
                preferences[CartValues.CART_ITEMS] = jsonProducts
            }
        }
    }

    override suspend fun removeFromCart(product: Product) {
        val productsString =
            dataStore.data
                .map { preferences -> preferences[CartValues.CART_ITEMS] }
                .firstOrNull()
        if (productsString != null) {
            val products = Json.decodeFromString<List<Product>>(productsString).toMutableList()
            products.remove(product)
            val jsonProducts = Json.encodeToString(products)

            dataStore.edit { preferences ->
                preferences[CartValues.CART_ITEMS] = jsonProducts
            }
        }
    }

    override fun getCartData(): Flow<List<Product>?> {
        val products =
            dataStore.data
                .map { preferences ->
                    preferences[CartValues.CART_ITEMS]
                }.map {
                    if (it != null) {
                        val ii = Json.decodeFromString<List<Product>>(it)
                        ii
                    } else {
                        null
                    }
                }

        return products
    }

    override suspend fun getProducts(table: String): List<Product> {
        try {
            val tables =
                TablesDB(
                    client = appWriteClient
                )
            val response =
                tables.listRows(
                    BuildConfig.DATABASE_ID,
                    table,
                    emptyList()
                )
            val data =
                response.rows.map { row ->
                    Product(
                        id = row.data["\$id"] as? String ?: "",
                        name = row.data["name"] as? String ?: "",
                        price = row.data["price"] as? String ?: "0.00",
                        description = row.data["description"] as? String ?: "",
                        imageUrl = row.data["imageUrl"] as? String,
                        type = row.data["type"] as? String ?: ""
                    )
                }

            return data
        } catch (e: Exception) {
            return emptyList()
        }
    }

    override suspend fun getProduct(productId: String?): Product? {
        try {
            val tables = TablesDB(client = appWriteClient)
            productId?.let { id ->
                val response =
                    tables.getRow(
                        BuildConfig.DATABASE_ID,
                        BuildConfig.PIZZA_COLLECTION_ID,
                        id
                    )
                val t = response.data["name"]

                return Product(
                    id = response.data["\$id"] as? String ?: "",
                    name = response.data["name"] as? String ?: "",
                    price = response.data["price"] as? String ?: "0.00",
                    description = response.data["description"] as? String ?: "",
                    imageUrl = response.data["imageUrl"] as? String ?: "",
                    type = response.data["type"] as? String ?: ""
                )
            }
        } catch (e: Exception) {
            null
        }

        return null
    }
}