package com.joshayoung.lazypizza.core.domain

interface LazyPizzaPreference {
    fun saveJwt(jwt: String)

    fun getJwt(): String
}