package com.joshayoung.lazypizza.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.joshayoung.lazypizza.core.ui.theme.BackIcon
import com.joshayoung.lazypizza.core.ui.theme.GrayPhone
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.LogoutIcon
import com.joshayoung.lazypizza.core.ui.theme.PizzaLogo
import com.joshayoung.lazypizza.core.ui.theme.UserIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzaAppBar(
    showLogo: Boolean = true,
    showContact: Boolean = true,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    authenticate: () -> Unit = {},
    title: String? = null,
    isAuthenticated: Boolean = false,
    showUserIcon: Boolean = false
) {
    var showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Are you sure you want to log out?") },
            confirmButton = {
                Button(onClick = {
                    // TODO: This should not be called directly from composable:
                    FirebaseAuth.getInstance().signOut()
                    showDialog.value = false
                }) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog.value = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    TopAppBar(
        modifier = Modifier,
        title = {
            Row(
                modifier =
                    Modifier
                        .padding(end = 10.dp)
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showBackButton) {
                    BackButton(onBackClick = onBackClick)
                }
                if (showLogo) {
                    Logo()
                }

                title?.let { pageTitle ->
                    Text(
                        pageTitle,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        modifier =
                            Modifier
                                .weight(1f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showContact) {
                        Contact()
                    }
                    if (showUserIcon) {
                        LoginStatusIcons(
                            isAuthenticated = isAuthenticated,
                            showDialog = showDialog,
                            authenticate = authenticate
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun LoginStatusIcons(
    isAuthenticated: Boolean,
    showDialog: MutableState<Boolean>,
    authenticate: () -> Unit
) {
    if (isAuthenticated) {
        IconButton(onClick = {
            showDialog.value = true
        }) {
            Icon(
                imageVector = LogoutIcon,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
                modifier =
                    Modifier
                        .padding(end = 8.dp)
            )
        }
    } else {
        IconButton(
            onClick =
                {
                    authenticate()
                }
        ) {
            Icon(
                imageVector = UserIcon,
                contentDescription = null,
                modifier =
                    Modifier
                        .padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun BackButton(onBackClick: () -> Unit) {
    Row {
        Box(
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.5f))
                    .size(30.dp)
        ) {
            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(
                    imageVector = BackIcon,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun Contact() {
    Row(
        modifier =
        Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = GrayPhone,
            contentDescription = null,
            tint = Color.Gray,
            modifier =
                Modifier
                    .padding(end = 8.dp)
        )
        Text(text = "+1 (555) 321-7890", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun Logo() {
    Row(
        modifier =
            Modifier
                .padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = PizzaLogo,
            contentDescription = null,
            modifier =
                Modifier
                    .padding(end = 10.dp)
        )
        Text(
            text = "LazyPizza",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun LazyPizzaAppBarPreview() {
    LazyPizzaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PizzaAppBar()
            PizzaAppBar(isAuthenticated = true)
            PizzaAppBar(
                showLogo = false,
                showContact = false,
                title = "Cart"
            )
            PizzaAppBar(
                showLogo = false,
                showContact = false,
                showBackButton = true
            )
        }
    }
}