package com.joshayoung.lazypizza.auth.domain.models

data class AuthState(
    val isLoggedIn: Boolean,
    val userId: String? = null
)