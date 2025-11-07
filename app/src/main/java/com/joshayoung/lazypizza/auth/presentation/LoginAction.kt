package com.joshayoung.lazypizza.auth.presentation

import android.app.Activity

sealed interface LoginAction {
    data class SetPhoneNumber(
        val number: String
    ) : LoginAction

    data class SendPhoneNumber(
        val activity: Activity?
    ) : LoginAction

    data class VerifySms(
        val code: String
    ) : LoginAction
}