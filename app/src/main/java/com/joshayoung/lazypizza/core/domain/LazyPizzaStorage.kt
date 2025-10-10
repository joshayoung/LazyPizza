package com.joshayoung.lazypizza.core.domain

interface LazyPizzaStorage {
    suspend fun getAllFiles(): List<String>
}