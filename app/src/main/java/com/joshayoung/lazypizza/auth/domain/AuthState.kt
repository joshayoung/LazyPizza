package com.joshayoung.lazypizza.auth.domain

data class AuthState(
    val isLoggedIn: Boolean,
    val userId: String? = null
)
