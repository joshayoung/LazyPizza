package com.joshayoung.lazypizza.core.domain

interface LazyPizzaAuth {
    suspend fun loginUser(
        email: String,
        password: String
    ): Boolean
}