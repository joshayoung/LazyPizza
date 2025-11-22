package com.joshayoung.lazypizza.core.data.network

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import com.joshayoung.lazypizza.core.domain.network.MenuRemoteDataSource
import io.appwrite.Client
import io.appwrite.services.TablesDB

class AppWriteMenuRemoteDataSource(
    private var appWriteClient: Client
) : MenuRemoteDataSource {
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

    override suspend fun getToppings(table: String): List<Topping> {
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
                    Topping(
                        remoteId = row.data["\$id"] as? String ?: "",
                        name = row.data["name"] as? String ?: "",
                        price = row.data["price"] as? String ?: "0.00",
                        imageUrl = row.data["imageUrl"] as? String
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
                        BuildConfig.MENU_ITEMS_COLLECTION_ID,
                        id
                    )

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