package com.joshayoung.lazypizza.app.domain.models

data class AppWriteState(
    val isLoggedIn: Boolean,
    val userId: String? = null
)