package com.joshayoung.lazypizza.core.domain

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): Boolean
}