package com.joshayoung.lazypizza.search.data.utils

import android.content.Context
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaDatabase
import com.joshayoung.lazypizza.core.domain.models.Product
import io.appwrite.Client
import io.appwrite.services.Databases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppWriteDatabase(
    private var context: Context
) : LazyPizzaDatabase {
    override suspend fun getTableData(table: String): List<Product> {
        val client =
            Client(context)
                .setEndpoint(BuildConfig.API_ENDPOINT)
                .setProject(BuildConfig.API_PROJECT_ID)

        val databases = Databases(client)
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    databases.listDocuments(
                        BuildConfig.DATABASE_ID,
                        table
                    )
                response.documents.map { document ->
                    Product(
                        name = document.data["name"] as? String ?: "",
                        price = document.data["price"] as? String ?: "0.00",
                        description = document.data["description"] as? String ?: "",
                        imageUrl = document.data["imageUrl"] as? String,
                        plImageUrl = document.data["plImageUrl"] as? String
                    )
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}