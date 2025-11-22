package com.joshayoung.lazypizza.core.domain

interface AuthRepository {
    suspend fun loginWithAppWrite(
        email: String,
        password: String
    ): Boolean

    fun logoutWithFirebase()
}