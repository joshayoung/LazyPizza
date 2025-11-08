package com.joshayoung.lazypizza.auth.presentation

data class LoginState(
    val isLoggingIn: Boolean = false,
    val phoneNumberSent: Boolean = false,
    val phoneNumberValid: Boolean = false,
    val verificationId: String? = null,
    val verificationFailed: Boolean = false,
    val numberSent: Boolean = false,
    val smsSent: Boolean = false,
    val phoneNumber: String = "",
    val countDown: String = "",
    val resend: Boolean = false
)
