package com.joshayoung.lazypizza.core.data.network

import android.util.Log
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.cart.domain.models.OrderDto
import com.joshayoung.lazypizza.cart.domain.models.OrderRequest
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
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

    override suspend fun placeOrder(orderRequest: OrderRequest): String? {
        val tablesDB = TablesDB(client = appWriteClient)

        try {
            val row =
                tablesDB.createRow(
                    BuildConfig.DATABASE_ID,
                    tableId = BuildConfig.ORDERS_COLLECTION_ID,
                    rowId = ID.unique(),
                    data =
                        mapOf(
                            "userId" to orderRequest.userId,
                            "orderNumber" to orderRequest.orderNumber,
                            "pickupTime" to orderRequest.pickupTime,
                            "items" to orderRequest.items,
                            "totalAmount" to orderRequest.checkoutPrice,
                            "status" to orderRequest.status
                        )
                )

            return row.id
        } catch (e: AppwriteException) {
            Log.e("Appwrite", "Error: " + e.message)
        }

        return null
    }

    override suspend fun getOrderInfo(id: String): OrderDto? {
        try {
            val tables = TablesDB(client = appWriteClient)
            val response =
                tables.getRow(
                    BuildConfig.DATABASE_ID,
                    BuildConfig.ORDERS_COLLECTION_ID,
                    id
                )
            val t = response.data["name"]

            return OrderDto(
                orderNumber = response.data["orderNumber"] as? String ?: "",
                pickupTime = response.data["pickupTime"] as? String ?: ""
            )
        } catch (e: Exception) {
            null
        }

        return null
    }

    override suspend fun getOrders(
        user: String,
        table: String
    ): List<OrderDto> {
        try {
            val tables =
                TablesDB(
                    client = appWriteClient
                )
            val response =
                tables.listRows(
                    BuildConfig.DATABASE_ID,
                    table,
                    queries =
                        listOf(
                            Query.equal("userId", user)
                        )
                )
            val data =
                response.rows.map { row ->
                    OrderDto(
//                        id = row.data["\$id"] as? String ?: "",
                        orderNumber = row.data["orderNumber"] as? String ?: "",
                        pickupTime = row.data["pickupTime"] as? String ?: "0.00"
                    )
                }

            return data
        } catch (e: Exception) {
            return emptyList()
        }
    }
}