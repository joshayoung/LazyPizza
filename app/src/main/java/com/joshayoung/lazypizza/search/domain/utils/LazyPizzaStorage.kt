package com.joshayoung.lazypizza.search.domain.utils

interface LazyPizzaStorage {
    suspend fun getAllFiles() : List<String>
}