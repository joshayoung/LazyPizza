package com.joshayoung.lazypizza.core.data

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaRepository
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.networking.JwtManager
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import io.appwrite.services.TablesDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppWriteRepository(
    private var appWriteClient: Client
) : LazyPizzaRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Boolean {
        val account = Account(appWriteClient)
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

    override suspend fun getTableData(table: String): List<Product> =
        withContext(Dispatchers.IO) {
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
                response.rows.map { row ->
                    Product(
                        name = row.data["name"] as? String ?: "",
                        price = row.data["price"] as? String ?: "0.00",
                        description = row.data["description"] as? String ?: "",
                        imageUrl = row.data["imageUrl"] as? String
                    )
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
}