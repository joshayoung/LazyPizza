package com.joshayoung.lazypizza.auth.presentation

sealed interface LoginAction {
    data class SetPhoneNumber(
        val number: String
    ) : LoginAction
}