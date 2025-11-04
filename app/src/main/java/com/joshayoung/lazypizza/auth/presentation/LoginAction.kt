package com.joshayoung.lazypizza.auth.presentation

sealed interface LoginAction {
    data class SendVerificationCode(
        var phoneNumber: String
    ) : LoginAction

    data class VerifyCode(
        var verificationCode: String
    ) : LoginAction
}