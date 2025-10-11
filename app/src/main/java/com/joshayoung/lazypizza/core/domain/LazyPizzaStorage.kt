package com.joshayoung.lazypizza.core.domain

interface LazyPizzaStorage {
    fun saveJwt(jwt: String)

    fun getJwt(): String
}