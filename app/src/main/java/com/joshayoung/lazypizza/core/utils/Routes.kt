package com.joshayoung.lazypizza.core.utils

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object Menu : Routes

    @Serializable
    data object Details : Routes

    @Serializable
    data object Login : Routes

    @Serializable
    data object Cart : Routes

    @Serializable
    data object History : Routes
}
