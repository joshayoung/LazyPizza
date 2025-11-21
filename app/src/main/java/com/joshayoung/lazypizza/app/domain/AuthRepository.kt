package com.joshayoung.lazypizza.app.domain

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): Boolean
}