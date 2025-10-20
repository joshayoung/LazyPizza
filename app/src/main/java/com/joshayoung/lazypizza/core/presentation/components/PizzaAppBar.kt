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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.ui.theme.GrayPhone
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.ui.theme.PizzaLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzaAppBar(
    showLogo: Boolean = true,
    showContact: Boolean = true,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    title: String? = null
) {
    TopAppBar(
        modifier = Modifier,
        title = {
            Row(
                modifier =
                    Modifier
                        .padding(end = 20.dp)
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

                if (showContact) {
                    Contact()
                }
            }
        }
    )
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
                    painter = painterResource(R.drawable.back),
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
        modifier = Modifier,
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