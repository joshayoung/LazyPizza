package com.joshayoung.lazypizza.cart.data.network

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.cart.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.domain.models.Product
import io.appwrite.Client
import io.appwrite.services.TablesDB

class AppWriteCartRemoteDataSource(
    private var appWriteClient: Client
) : CartRemoteDataSource {
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