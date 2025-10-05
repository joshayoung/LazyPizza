package com.joshayoung.lazypizza.search.presentation.searchItems

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.search.ImageResource
import com.joshayoung.lazypizza.search.presentation.components.LazyImage
import com.joshayoung.lazypizza.ui.theme.GrayPhone
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.ui.theme.PizzaLogo
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchItemsScreenRoot(viewModel: SearchItemsViewModel = koinViewModel()) {
    Column {
        Spacer(Modifier.height(100.dp))
        SearchItemsScreen(
            state = viewModel.state.collectAsStateWithLifecycle().value
        )
    }
}

@Composable
fun SearchItemsScreen(state: SearchItemsState) {
    Scaffold(
        topBar = {
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
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .background(Color(0xFFFAFBFC))
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
        ) {
            Image(
                painterResource(id = R.drawable.pizza_header),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier =
                    Modifier
                        .fillMaxWidth()
            )
            BasicTextField(
                state =
                    TextFieldState(
                        initialText = "Search for delicious food…"
                    )
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.images) { imageUrl ->
                        ItemAndPrice(imageUrl)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemAndPrice(image: ImageResource) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
        modifier =
            Modifier
                .height(140.dp)
                .fillMaxWidth()
    ) {
        Row(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyImage(image)
            Column(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(start = 20.dp)
                        .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Margherita", fontWeight = FontWeight.Bold)
                Text("Tomato sauce, mozzarella, fresh basil, olive oil")
                Text("$8.99", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchItemsScreenPreview() {
    LazyPizzaTheme {
        SearchItemsScreen(
            state =
                SearchItemsState(
                    images =
                        listOf(
                            ImageResource.DrawableResource(R.drawable.hawaiian),
                            ImageResource.DrawableResource(R.drawable.meat_lovers)
                        )
                )
        )
    }
}
