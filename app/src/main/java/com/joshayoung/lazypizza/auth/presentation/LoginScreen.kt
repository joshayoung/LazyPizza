package com.joshayoung.lazypizza.auth.presentation

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val activity = LocalActivity.current

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
            text = if (state.codeSent) "Enter Code" else "Enter your phone number",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        OutlinedTextField(
            placeholder = { Text("+1 000 000 0000") },
            value = state.phoneNumber,
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone
                ),
            onValueChange = {
                val filteredPhoneNumber =
                    it.filter { char ->
                        char.isDigit() || char == '+' || char.isWhitespace()
                    }
                if (filteredPhoneNumber.length > 15) {
                    return@OutlinedTextField
                }

                onAction(LoginAction.SetPhoneNumber(filteredPhoneNumber))
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceHighest)
        )

        if (state.codeSent) {
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

            var smsBorder = Color.Transparent
            if (state.verificationFailed) {
                smsBorder = MaterialTheme.colorScheme.primary
            }

            Row(
                modifier =
                    Modifier
                        .padding(top = 14.dp)
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceHighest)
                            .border(
                                width = 1.dp,
                                color = smsBorder,
                                shape = RoundedCornerShape(20.dp)
                            ).focusRequester(focus1)
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
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceHighest)
                            .border(
                                width = 1.dp,
                                color = smsBorder,
                                shape = RoundedCornerShape(20.dp)
                            ).focusRequester(focus2)
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
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceHighest)
                            .border(
                                width = 1.dp,
                                color = smsBorder,
                                shape = RoundedCornerShape(20.dp)
                            ).focusRequester(focus3)
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
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceHighest)
                            .border(
                                width = 1.dp,
                                color = smsBorder,
                                shape = RoundedCornerShape(20.dp)
                            ).focusRequester(focus4)
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
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceHighest)
                            .border(
                                width = 1.dp,
                                color = smsBorder,
                                shape = RoundedCornerShape(20.dp)
                            ).focusRequester(focus5)
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
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceHighest)
                            .border(
                                width = 1.dp,
                                color = smsBorder,
                                shape = RoundedCornerShape(20.dp)
                            ).focusRequester(focus6)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused && code5.isEmpty()) {
                                    focus5.requestFocus()
                                }
                            }
                )
            }

            Button(
                onClick = {
                    val verificationCode = "$code1$code2$code3$code4$code5$code6"
                    onAction(LoginAction.VerifySms(verificationCode))
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
                    onAction(LoginAction.SendPhoneNumber(activity))
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
