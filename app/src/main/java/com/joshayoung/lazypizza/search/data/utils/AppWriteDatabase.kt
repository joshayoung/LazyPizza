package com.joshayoung.lazypizza.search.data.utils

import android.content.Context
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.search.data.models.Product
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaDatabase
import io.appwrite.Client
import io.appwrite.services.Databases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppWriteDatabase(
    private var context: Context
) : LazyPizzaDatabase {
    override suspend fun getAllData(): List<Product> {
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
                        BuildConfig.COLLECTION_ID
                    )
                response.documents.map { document ->
                    Product(
                        name = document.data["name"] as? String ?: "",
                        price = document.data["price"] as? String ?: "0.00",
                        description = document.data["description"] as? String ?: "",
                        imageUrl = document.data["imageUrl"] as? String ?: ""
                    )
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}