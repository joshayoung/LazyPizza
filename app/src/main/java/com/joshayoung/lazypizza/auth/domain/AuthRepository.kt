package com.joshayoung.lazypizza.auth.domain

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): Boolean
}