package com.joshayoung.lazypizza.search.presentation.search_items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.ui.theme.GrayPhone
import com.joshayoung.lazypizza.ui.theme.PhoneIcon
import com.joshayoung.lazypizza.ui.theme.PizzaHeader
import com.joshayoung.lazypizza.ui.theme.PizzaLogo
import kotlin.contracts.contract

@Composable
fun SearchItemsScreen() {
    Text("Search Items")
    Scaffold(
        topBar = {
            Row(modifier = Modifier
                .fillMaxWidth()
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row( modifier = Modifier
                    .background(Color.Red)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(imageVector = PizzaLogo, contentDescription = null
                        , modifier = Modifier
                            .padding(end = 10.dp)
                    )
                    Text(text = "LazyPizza",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold

                        )
                }
                Row(modifier = Modifier
                    , verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = GrayPhone, contentDescription = null, tint = Color.Gray,
                        modifier = Modifier
                            .padding(end = 10.dp)
                        )
                    Text(text = "+1 (555) 321-7890")
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
        ) {
            Image(painterResource(id = R.drawable.pizza_header),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                )
            BasicTextField(
                state = TextFieldState(
                    initialText = "Search for delicious food…"
                )
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SearchItemsScreenPreview() {
    SearchItemsScreen()
}