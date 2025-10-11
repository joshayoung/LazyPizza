package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaRepository
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.networking.JwtManager
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthWriteRepository(
    private var authWriteClient: Client
) : LazyPizzaRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Boolean {
        val account = Account(authWriteClient)
        try {
            val currentSession = account.getSession("current")

            return currentSession.current
        } catch (_: AppwriteException) {
            return try {
                account.createEmailPasswordSession(email, password)
                val token = account.createJWT()
                JwtManager.token = token.jwt

                return true
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun getTableData(table: String): List<Product> {
        val databases = Databases(authWriteClient)
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