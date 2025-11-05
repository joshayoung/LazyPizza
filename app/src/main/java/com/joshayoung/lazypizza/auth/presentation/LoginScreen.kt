package com.joshayoung.lazypizza.auth.presentation

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.PhoneAuthCredential
import com.joshayoung.lazypizza.core.data.database.entity.FirebaseAuthenticatorUiClient
import com.joshayoung.lazypizza.core.data.database.entity.FirebaseAuthenticatorUiClientCallback
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.surfaceHighest
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    useAsGuest: () -> Unit
) {
    LoginScreen(
        state = viewModel.state,
        useAsGuest = useAsGuest,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    useAsGuest: () -> Unit
) {
    var sent by remember { mutableStateOf(false) }
    var number by remember { mutableStateOf("") }

    val id = remember { mutableStateOf("") }

    val context = LocalContext.current

    val firebaseAuthenticatorUiClient =
        FirebaseAuthenticatorUiClient(
            object : FirebaseAuthenticatorUiClientCallback {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // TODO: Update state on success.
                }

                override fun onVerificationFailed(message: String) {
                    // TODO: Update state on failure.
                }

                override fun onCodeSent(verificationId: String) {
                    id.value = verificationId
                }
            }
        )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 200.dp)
                .padding(horizontal = 10.dp)
    ) {
        Text(
            text = "Welcome to LazyPizza",
            style = MaterialTheme.typography.titleLarge,
            modifier =
                Modifier
                    .padding(bottom = 6.dp)
        )
        Text(
            text = if (sent) "Enter Code" else "Enter your phone number",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        OutlinedTextField(
            placeholder = { Text("+1 000 000 0000") },
            value = number,
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone
                ),
            onValueChange = {
                val allowedValue =
                    it.filter { char ->
                        char.isDigit() || char == '+' || char.isWhitespace()
                    }
                if (allowedValue.length > 15) {
                    return@OutlinedTextField
                }

                number = allowedValue
                onAction(LoginAction.SetPhoneNumber(number))
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceHighest)
        )

        if (sent) {
            var code1 by remember { mutableStateOf("") }
            var code2 by remember { mutableStateOf("") }
            var code3 by remember { mutableStateOf("") }
            var code4 by remember { mutableStateOf("") }
            var code5 by remember { mutableStateOf("") }
            var code6 by remember { mutableStateOf("") }

            val focus1 = remember { FocusRequester() }
            val focus2 = remember { FocusRequester() }
            val focus3 = remember { FocusRequester() }
            val focus4 = remember { FocusRequester() }
            val focus5 = remember { FocusRequester() }
            val focus6 = remember { FocusRequester() }

            Row(
                modifier =
                    Modifier
                        .padding(top = 14.dp)
                        .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = code1,
                    onValueChange = {
                        code1 = it
                        if (it.isNotEmpty()) {
                            focus2.requestFocus()
                        }
                    },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                    modifier =
                        Modifier
                            .weight(1f)
                            .focusRequester(focus1)
                )
                OutlinedTextField(
                    value = code2,
                    onValueChange = {
                        code2 = it
                        if (it.isNotEmpty()) {
                            focus3.requestFocus()
                        }
                    },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                    modifier =
                        Modifier
                            .weight(1f)
                            .focusRequester(focus2)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused && code1.isEmpty()) {
                                    focus1.requestFocus()
                                }
                            }
                )
                OutlinedTextField(
                    value = code3,
                    onValueChange = {
                        code3 = it
                        if (it.isNotEmpty()) {
                            focus4.requestFocus()
                        }
                    },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                    modifier =
                        Modifier
                            .weight(1f)
                            .focusRequester(focus3)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused && code2.isEmpty()) {
                                    focus2.requestFocus()
                                }
                            }
                )
                OutlinedTextField(
                    value = code4,
                    onValueChange = {
                        code4 = it
                        if (it.isNotEmpty()) {
                            focus5.requestFocus()
                        }
                    },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                    modifier =
                        Modifier
                            .weight(1f)
                            .focusRequester(focus4)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused && code3.isEmpty()) {
                                    focus3.requestFocus()
                                }
                            }
                )
                OutlinedTextField(
                    value = code5,
                    onValueChange = {
                        code5 = it
                        if (it.isNotEmpty()) {
                            focus6.requestFocus()
                        }
                    },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                    modifier =
                        Modifier
                            .weight(1f)
                            .focusRequester(focus5)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused && code4.isEmpty()) {
                                    focus4.requestFocus()
                                }
                            }
                )
                OutlinedTextField(
                    value = code6,
                    onValueChange = {
                        if (code6.isEmpty()) {
                            code6 = it
                        }
                    },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                    modifier =
                        Modifier
                            .weight(1f)
                            .focusRequester(focus6)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused && code5.isEmpty()) {
                                    focus5.requestFocus()
                                }
                            }
                )
            }

            Button(
                onClick = {
                    try {
                        firebaseAuthenticatorUiClient.verifyCode(
                            "${code1}${code2}${code3}${code4}${code5}$code6",
                            id.value
                        )
                        sent = true
                    } catch (e: Exception) {
                        println()
                    }
                },
                modifier =
                    Modifier
                        .padding(top = 14.dp)
                        .fillMaxWidth()
            ) {
                Text(text = "Confirm")
            }
        } else {
            Button(
                onClick = {
                    val activity = context as? Activity
                    if (activity != null) {
                        firebaseAuthenticatorUiClient.startPhoneVerification(number, activity)
                    }

                    sent = true
                },
                enabled = state.phoneNumberValid,
                modifier =
                    Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth()
            ) {
                Text(text = "Continue")
            }
        }
        Text(
            modifier =
                Modifier
                    .clickable {
                        useAsGuest()
                    },
            text = "Continue without signing in",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun SmsField(
    previousCode: MutableState<String>?,
    previousFocusRequester: FocusRequester?,
    code: MutableState<String>,
    focusRequester: FocusRequester,
    focusRequesterNext: FocusRequester,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    OutlinedTextField(
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
        value = code.value,
        onValueChange = {
            if (code.value.isNotEmpty()) {
                return@OutlinedTextField
            }
            code.value = it.replace("\t", "")
        },
        textStyle =
            TextStyle(
                textAlign = TextAlign.Center
            ),
        placeholder = {
            if (!isFocused) {
                Text(
                    "0",
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    style =
                        TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp
                        )
                )
            }
        },
        modifier =
            modifier
                .padding(horizontal = 2.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceHighest)
                .onFocusChanged { focused ->
                    isFocused = focused.isFocused
                    if (previousCode != null && previousFocusRequester != null) {
                        if (previousCode.value.isEmpty()) {
                            focusRequester.freeFocus()
                            previousFocusRequester.requestFocus()
                        }
                    }

//                    if (focused.isFocused && code.value.isEmpty()) {
//                        focusRequester.requestFocus()
//                    }
                }
//                .focusOrder(focusRequester) {
//                    next = focusRequesterNext
//                }.onKeyEvent { event ->
//                    if (event.key == Key.Tab) {
//                        focusRequesterNext.requestFocus()
//                        true
//                    } else {
//                        false
//                    }
//                }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LazyPizzaTheme {
        LoginScreen(
            useAsGuest = {},
            state = LoginState(),
            onAction = {}
        )
    }
}
