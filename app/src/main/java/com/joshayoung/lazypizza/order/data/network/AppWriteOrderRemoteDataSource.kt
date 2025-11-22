package com.joshayoung.lazypizza.order.data.network

import android.util.Log
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.cart.domain.models.OrderRequest
import com.joshayoung.lazypizza.order.domain.network.OrderRemoteDataSource
import com.joshayoung.lazypizza.order.domain.network.models.OrderDto
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.TablesDB

class AppWriteOrderRemoteDataSource(
    private var appWriteClient: Client
) : OrderRemoteDataSource {
    override suspend fun placeOrder(orderRequest: OrderRequest): String? {
        val tablesDB = TablesDB(client = appWriteClient)

        try {
            val row =
                tablesDB.createRow(
                    BuildConfig.DATABASE_ID,
                    tableId = BuildConfig.ORDERS_COLLECTION_ID,
                    rowId = ID.Companion.unique(),
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

            return OrderDto(
                userId = response.data["userId"] as? String ?: "",
                orderNumber = response.data["orderNumber"] as? String ?: "",
                pickupTime = response.data["pickupTime"] as? String ?: "",
                items = response.data["items"] as? String ?: "",
                totalAmount = response.data["totalAmount"] as? String ?: "",
                status = response.data["status"] as? String ?: "",
                createdAt = response.data["\$createdAt"] as? String ?: ""
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
                            Query.Companion.equal("userId", user)
                        )
                )
            val data =
                response.rows.map { row ->
                    OrderDto(
                        userId = row.data["userId"] as? String ?: "",
                        orderNumber = row.data["orderNumber"] as? String ?: "",
                        pickupTime = row.data["pickupTime"] as? String ?: "",
                        items = row.data["items"] as? String ?: "",
                        totalAmount = row.data["totalAmount"] as? String ?: "",
                        status = row.data["status"] as? String ?: "",
                        createdAt = row.data["\$createdAt"] as? String ?: ""
                    )
                }

            return data
        } catch (e: Exception) {
            return emptyList()
        }
    }
}