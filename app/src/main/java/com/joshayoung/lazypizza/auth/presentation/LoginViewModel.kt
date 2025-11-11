package com.joshayoung.lazypizza.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.CartRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {
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
                state =
                    state.copy(
                        isSendingPhoneNumber = true
                    )
                action.activity?.let { activity ->
                    viewModelScope.launch {
                        // TODO: Add Error Handling:
                        val verification =
                            firebaseAuthUiClient
                                .verifyPhoneNumber(
                                    activity,
                                    state.phoneNumber
                                )
                        state =
                            state.copy(
                                verificationId = verification,
                                numberSentSuccessfully = true,
                                resend = false
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
                                        numberSentSuccessfully = false,
                                        verificationFailed = false,
                                        resend = true
                                    )
                            }
                        }
                    }
                }
                state =
                    state.copy(
                        isSendingPhoneNumber = false
                    )
            }
            is LoginAction.VerifySms -> {
                state =
                    state.copy(
                        isLoggingIn = true
                    )
                viewModelScope.launch {
                    val loggedIn =
                        firebaseAuthUiClient
                            .sendCode(
                                state.verificationId,
                                state.verificationCode
                            )
                    state =
                        state.copy(
                            // TODO: Do I need both of these?
                            verificationFailed = !loggedIn
                        )

                    if (loggedIn) {
                        cartRepository.transferCartTo("guest-user", FirebaseAuth.getInstance().uid)
                    }

                    state =
                        state.copy(
                            isLoggingIn = false
                        )
                }
            }

            is LoginAction.SetCode1 -> {
                state =
                    state.copy(
                        code1 = action.code
                    )
            }

            is LoginAction.SetCode2 -> {
                state =
                    state.copy(
                        code2 = action.code
                    )
            }
            is LoginAction.SetCode3 -> {
                state =
                    state.copy(
                        code3 = action.code
                    )
            }
            is LoginAction.SetCode4 -> {
                state =
                    state.copy(
                        code4 = action.code
                    )
            }
            is LoginAction.SetCode5 -> {
                state =
                    state.copy(
                        code5 = action.code
                    )
            }
            is LoginAction.SetCode6 -> {
                state =
                    state.copy(
                        code6 = action.code
                    )

                enableButtonOnceAllCodesAreEntered()
            }
        }
    }

    fun enableButtonOnceAllCodesAreEntered() {
        var entered = false

        val codeFields =
            listOf(
                state.code1,
                state.code2,
                state.code3,
                state.code4,
                state.code5,
                state.code6
            )

        codeFields.forEach { field ->
            if (field.isEmpty()) {
                entered = false
                return@forEach
            }
            entered = true
        }

        state =
            state.copy(
                codeEntered = entered
            )
    }

    fun countDownFlow(): Flow<Int> =
        flow {
            var counter = 60

            if (inDebug()) {
                counter = 20
            }

            while (counter >= 0) {
                emit(counter)
                delay(1000)
                counter--
            }
        }

    private fun inDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}