package com.joshayoung.lazypizza.app.data

import com.joshayoung.lazypizza.app.domain.AuthRepository
import com.joshayoung.lazypizza.core.networking.JwtManager
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account

class AppWriteAuthRepository(
    private var appWriteClient: Client
) : AuthRepository {
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
}