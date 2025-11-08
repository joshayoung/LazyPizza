package com.joshayoung.lazypizza.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
                                numberSent = true
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
                            // TODO: Do I need both of these?
                            verificationFailed = !result,
                            smsSent = true
                        )

                    countDownFlow().collect {
                        var time = it.toString()
                        if (time.length < 2) {
                            time = "0$time"
                        }
                        state =
                            state.copy(
                                countDown = "00:$time"
                            )
                        if (it < 1) {
                            state =
                                state.copy(
                                    resend = true
                                )
                        }
                    }
                }
            }
        }
    }

    fun countDownFlow(): Flow<Int> =
        flow {
            var counter = 60
            while (counter >= 0) {
                emit(counter)
                delay(1000)
                counter--
            }
        }
}