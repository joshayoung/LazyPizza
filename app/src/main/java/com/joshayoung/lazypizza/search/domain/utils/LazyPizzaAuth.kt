package com.joshayoung.lazypizza.search.domain.utils

interface LazyPizzaAuth {
    suspend fun loginUser(
        email: String,
        password: String
    ): Boolean
}