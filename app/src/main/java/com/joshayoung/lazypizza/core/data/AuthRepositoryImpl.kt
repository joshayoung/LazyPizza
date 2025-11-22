package com.joshayoung.lazypizza.core.data

import com.google.firebase.auth.FirebaseAuth
import com.joshayoung.lazypizza.core.domain.AuthRepository
import com.joshayoung.lazypizza.core.networking.JwtManager
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account

class AuthRepositoryImpl(
    private var appWriteClient: Client,
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override suspend fun loginWithAppWrite(
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

    override fun logoutWithFirebase() {
        firebaseAuth.signOut()
    }
}