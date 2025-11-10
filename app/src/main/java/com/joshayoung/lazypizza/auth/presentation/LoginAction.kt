package com.joshayoung.lazypizza.auth.presentation

import android.app.Activity

sealed interface LoginAction {
    data class SetPhoneNumber(
        val number: String
    ) : LoginAction

    data class SendPhoneNumber(
        val activity: Activity?
    ) : LoginAction

    data object VerifySms : LoginAction

    data class SetCode1(
        val code: String
    ) : LoginAction

    data class SetCode2(
        val code: String
    ) : LoginAction

    data class SetCode3(
        val code: String
    ) : LoginAction

    data class SetCode4(
        val code: String
    ) : LoginAction

    data class SetCode5(
        val code: String
    ) : LoginAction

    data class SetCode6(
        val code: String
    ) : LoginAction
}