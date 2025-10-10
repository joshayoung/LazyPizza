package com.joshayoung.lazypizza.search.data.utils

import android.content.Context
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaAuth
import com.joshayoung.lazypizza.core.domain.LazyPizzaPreference
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account

class AppWriteAuth(
    private var context: Context,
    private var lazyPizzaPreference: LazyPizzaPreference
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
            val currentSession = account.getSession("current")

            return currentSession.current
        } catch (_: AppwriteException) {
            return try {
                account.createEmailPasswordSession(email, password)
                val token = account.createJWT()
                lazyPizzaPreference.saveJwt(token.jwt)

                return true
            } catch (e: Exception) {
                false
            }
        }
    }
}