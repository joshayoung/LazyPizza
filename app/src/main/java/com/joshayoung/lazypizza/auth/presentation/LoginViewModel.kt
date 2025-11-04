package com.joshayoung.lazypizza.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel(
    private var firebaseAuthenticator: FirebaseAuthenticator
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.SendVerificationCode -> {
                val t = firebaseAuthenticator.sendCode(action.phoneNumber)
            }

            is LoginAction.VerifyCode -> {
                val t = firebaseAuthenticator.verifyCode(action.verificationCode)
            }
        }
    }
}