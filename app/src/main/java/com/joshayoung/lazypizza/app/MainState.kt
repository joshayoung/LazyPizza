package com.joshayoung.lazypizza.app

data class MainState(
    val isLoading: Boolean = false,
    val cartItems: Int = 0
)
