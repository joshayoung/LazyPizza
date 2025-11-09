package com.joshayoung.lazypizza.auth.presentation

data class LoginState(
    val phoneNumberValid: Boolean = false,
    val verificationId: String? = null,
    val verificationFailed: Boolean = false,
    val numberSentSuccessfully: Boolean = false,
    val phoneNumber: String = "",
    val countDown: String = "",
    val resend: Boolean = false
)
