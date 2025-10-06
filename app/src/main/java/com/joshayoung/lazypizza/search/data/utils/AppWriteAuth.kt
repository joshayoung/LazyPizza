package com.joshayoung.lazypizza.search.data.utils

import android.content.Context
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaAuth
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppWriteAuth(
    private var context: Context
) : LazyPizzaAuth {
    override suspend fun loginUser(
        email: String,
        password: String
    ): Boolean {
        val client =
            Client(context)
                .setEndpoint(BuildConfig.API_ENDPOINT)
                .setProject(BuildConfig.API_PROJECT_ID)

        val account = Account(client)
        try {
            val userData =
                withContext(Dispatchers.IO) {
                    account.get()
                }

            if (userData.status) {
                return true
            }
        } catch (_: AppwriteException) {
            return try {
                val session = account.createEmailPasswordSession(email, password)
                session.current
            } catch (e: Exception) {
                false
            }
        }

        return false
    }
}