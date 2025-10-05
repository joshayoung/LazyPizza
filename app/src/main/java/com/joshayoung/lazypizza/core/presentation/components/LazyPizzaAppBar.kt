package com.joshayoung.lazypizza.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaScaffold
import com.joshayoung.lazypizza.ui.theme.GrayPhone
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.ui.theme.PizzaLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyPizzaAppBar() {
    TopAppBar(
        title = {
            Row(
                modifier =
                    Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier =
                    Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                                .padding(end = 10.dp)
                    )
                    Text(text = "+1 (555) 321-7890", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    )
}

@Preview
@Composable
fun LazyPizzaAppBarPreview() {
    LazyPizzaTheme {
        LazyPizzaAppBar()
    }
}