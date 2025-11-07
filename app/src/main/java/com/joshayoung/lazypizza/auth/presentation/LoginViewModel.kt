package com.joshayoung.lazypizza.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set
    private val firebaseAuthUiClient: FirebaseAuthUiClient = FirebaseAuthUiClient()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.SetPhoneNumber -> {
                val regex = Regex("^\\+\\d \\d{3} \\d{3} \\d{4}$")
                val isMatch = regex.matches(action.number)
                state =
                    state.copy(
                        phoneNumberValid = isMatch,
                        phoneNumber = action.number
                    )
            }

            is LoginAction.SendPhoneNumber -> {
                action.activity?.let { activity ->
                    viewModelScope.launch {
                        val verification =
                            firebaseAuthUiClient
                                .verifyPhoneNumber(
                                    activity,
                                    state.phoneNumber
                                )
                        state =
                            state.copy(
                                verificationId = verification,
                                codeSent = true
                            )
                    }
                }
            }
            is LoginAction.VerifySms -> {
                viewModelScope.launch {
                    val result =
                        firebaseAuthUiClient
                            .sendCode(
                                state.verificationId,
                                action.code
                            )
                    state =
                        state.copy(
                            verificationFailed = !result
                        )
                }
            }
        }
    }
}