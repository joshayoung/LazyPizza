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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
            val code1 = remember { mutableStateOf(TextFieldValue("")) }
            val code2 = remember { mutableStateOf(TextFieldValue("")) }
            val code3 = remember { mutableStateOf(TextFieldValue("")) }
            val code4 = remember { mutableStateOf(TextFieldValue("")) }
            val code5 = remember { mutableStateOf(TextFieldValue("")) }
            val code6 = remember { mutableStateOf(TextFieldValue("")) }

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
                SmsTextField(
                    code1,
                    borderColor = smsBorder,
                    codeFocus = focus1,
                    nextFocus = focus2,
                    modifier =
                        Modifier
                            .weight(1f)
                )
                SmsTextField(
                    code2,
                    borderColor = smsBorder,
                    codeFocus = focus2,
                    nextFocus = focus3,
                    previousCode = code1,
                    previousFocus = focus1,
                    modifier =
                        Modifier
                            .weight(1f)
                )
                SmsTextField(
                    code3,
                    borderColor = smsBorder,
                    codeFocus = focus3,
                    nextFocus = focus4,
                    previousCode = code2,
                    previousFocus = focus2,
                    modifier =
                        Modifier
                            .weight(1f)
                )
                SmsTextField(
                    code4,
                    borderColor = smsBorder,
                    codeFocus = focus4,
                    nextFocus = focus5,
                    previousCode = code3,
                    previousFocus = focus3,
                    modifier =
                        Modifier
                            .weight(1f)
                )
                SmsTextField(
                    code5,
                    borderColor = smsBorder,
                    codeFocus = focus5,
                    nextFocus = focus6,
                    previousCode = code4,
                    previousFocus = focus4,
                    modifier =
                        Modifier
                            .weight(1f)
                )
                SmsTextField(
                    code6,
                    borderColor = smsBorder,
                    codeFocus = focus6,
                    nextFocus = focus1,
                    previousCode = code5,
                    previousFocus = focus5,
                    modifier =
                        Modifier
                            .weight(1f)
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

@Composable
fun SmsTextField(
    code: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    borderColor: Color,
    codeFocus: FocusRequester,
    nextFocus: FocusRequester,
    previousCode: MutableState<TextFieldValue>? = null,
    previousFocus: FocusRequester? = null
) {
    OutlinedTextField(
        value = code.value,
        onValueChange = {
            if (it.text.length <= 1) {
                code.value = it
            }
            if (it.text.length == 1) {
                nextFocus.requestFocus()
            }
        },
        keyboardOptions =
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        shape = RoundedCornerShape(20.dp),
        modifier =
            modifier
                .background(
                    MaterialTheme.colorScheme.surfaceHighest,
                    shape = RoundedCornerShape(20.dp)
                ).border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(20.dp)
                ).focusRequester(codeFocus)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused && previousCode?.value?.text?.isEmpty() ?: false) {
                        previousFocus?.requestFocus()
                    }
                    if (focusState.isFocused) {
                        code.value =
                            TextFieldValue(
                                code.value.text,
                                selection = TextRange(0, code.value.text.length)
                            )
                    }
                }
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
