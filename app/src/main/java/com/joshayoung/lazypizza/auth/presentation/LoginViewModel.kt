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
                        phoneNumberValid = isMatch
                    )
            }

            is LoginAction.SendPhoneNumber -> {
                action.activity?.let { activity ->
                    viewModelScope.launch {
                        firebaseAuthUiClient
                            .verifyPhoneNumber(
                                activity,
                                action.number
                            ).collectLatest { verification ->
                                state =
                                    state.copy(
                                        verificationId = verification
                                    )
                            }
                    }
                }
            }
            is LoginAction.VerifySms -> {
                viewModelScope.launch {
                    firebaseAuthUiClient
                        .sendCode(
                            state.verificationId,
                            action.code
                        ).collectLatest { result ->
                            state =
                                state.copy(
                                    verificationFailed = !result
                                )
                        }
                }
            }
        }
    }
}