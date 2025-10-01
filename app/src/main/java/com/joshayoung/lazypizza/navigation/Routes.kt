package com.joshayoung.lazypizza.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object UsernameScreen: Routes
}