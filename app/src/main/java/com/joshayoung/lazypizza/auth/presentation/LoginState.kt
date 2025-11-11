package com.joshayoung.lazypizza.auth.presentation

data class LoginState(
    val phoneNumberValid: Boolean = false,
    val verificationId: String? = null,
    val verificationFailed: Boolean = false,
    val numberSentSuccessfully: Boolean = true,
    val phoneNumber: String = "",
    val countDown: String = "",
    val codeEntered: Boolean = false,
    val resend: Boolean = false,
    val code1: String = "",
    val code2: String = "",
    val code3: String = "",
    val code4: String = "",
    val code5: String = "",
    val code6: String = "",
    val isLoggingIn: Boolean = false,
    val isSendingPhoneNumber: Boolean = false
) {
    val verificationCode: String
        get() = "$code1$code2$code3$code4$code5$code6"
}
