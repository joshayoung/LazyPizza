package com.joshayoung.lazypizza.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object Menu : Routes

    @Serializable
    data object Details : Routes

    @Serializable
    data object Cart : Routes

    @Serializable
    data object History : Routes
}
