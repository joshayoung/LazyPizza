package com.joshayoung.lazypizza.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.auth.ObserveAsEvents
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    useAsGuest: () -> Unit,
    submitPhoneNumber: (number: String) -> Unit,
    verifyCode: (code: String) -> Unit
) {
    LoginScreen(
        state = viewModel.state,
        useAsGuest = useAsGuest,
        onAction = { action ->
            viewModel.onAction(action)
        },
        submitPhoneNumber = submitPhoneNumber,
        verifyCode = verifyCode
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    useAsGuest: () -> Unit,
    submitPhoneNumber: (number: String) -> Unit,
    verifyCode: (code: String) -> Unit
) {
    var sent by remember { mutableStateOf(false) }
    var number by remember { mutableStateOf("") }
    val code1 = remember { mutableStateOf("") }
    val code2 = remember { mutableStateOf("") }
    val code3 = remember { mutableStateOf("") }
    val code4 = remember { mutableStateOf("") }
    val code5 = remember { mutableStateOf("") }
    val code6 = remember { mutableStateOf("") }

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
            text = "Enter your phone number",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        OutlinedTextField(
            placeholder = { Text("Enter Phone Number") },
            value = number,
            onValueChange = {
                number = it
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        if (sent) {
            val nextFocus = remember { FocusRequester() }
            val nextFocus1 = remember { FocusRequester() }
            val nextFocus2 = remember { FocusRequester() }
            val nextFocus3 = remember { FocusRequester() }
            val nextFocus4 = remember { FocusRequester() }
            val nextFocus5 = remember { FocusRequester() }

            Row {
                SmsField(code1, nextFocus, nextFocus1)
                SmsField(code2, nextFocus1, nextFocus2)
                SmsField(code3, nextFocus2, nextFocus3)
                SmsField(code4, nextFocus3, nextFocus4)
                SmsField(code5, nextFocus4, nextFocus5)
                SmsField(code6, nextFocus5, nextFocus)
            }

            Button(
                onClick = {
                    verifyCode(
                        code1.value + code2.value + code3.value +
                            code4.value + code5.value + code6.value
                    )
                    sent = true
                },
                modifier =
                    Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth()
            ) {
                Text(text = "Confirm")
            }
        } else {
            Button(
                onClick = {
                    submitPhoneNumber(number)
                    sent = true
                },
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
    code: MutableState<String>,
    focusRequester: FocusRequester,
    focusRequesterNext: FocusRequester
) {
    OutlinedTextField(
        value = code.value,
        onValueChange = {
            code.value = it.replace("\t", "")
        },
        placeholder = { Text("0") },
        modifier =
            Modifier
                .width(60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .focusOrder(focusRequester) {
                    next = focusRequesterNext
                }.onKeyEvent { event ->
                    if (event.key == Key.Tab) {
                        focusRequesterNext.requestFocus()
                        true
                    } else {
                        false
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
            onAction = {},
            verifyCode = {},
            submitPhoneNumber = {}
        )
    }
}
