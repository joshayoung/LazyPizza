package com.joshayoung.lazypizza.search.presentation.searchItems

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaScaffold
import com.joshayoung.lazypizza.search.ImageResource
import com.joshayoung.lazypizza.search.data.models.Product
import com.joshayoung.lazypizza.search.presentation.components.LazyImage
import com.joshayoung.lazypizza.search.presentation.components.SearchField
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchItemsScreenRoot(viewModel: SearchItemsViewModel = koinViewModel()) {
    SearchItemsScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value
    )
}

@Composable
fun SearchItemsScreen(state: SearchItemsState) {
    LazyPizzaScaffold(
        topAppBar = { LazyPizzaAppBar() }
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .background(Color(0xFFFAFBFC))
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
            SearchField(state.search)
            val options = listOf("Pizza", "Drinks", "Sauces", "Ice Cream")
            val selectedIndex by remember { mutableIntStateOf(-1) }
            Row {
                options.forEachIndexed { index, label ->
                    val selected = index == selectedIndex
                    AssistChip(
                        modifier =
                            Modifier
                                .padding(end = 4.dp),
                        onClick = {},
                        label = { Text(label) },
                        leadingIcon = null,
                        shape = RoundedCornerShape(8.dp),
                        colors =
                            AssistChipDefaults.assistChipColors(
                                containerColor =
                                    if (selected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    },
                                labelColor =
                                    if (selected) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                            )
                    )
                }
            }
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.products) { product ->
                        ItemAndPrice(product, state)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemAndPrice(
    product: Product,
    state: SearchItemsState
) {
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
            LazyImage(
                if (product.imageUrl != null) {
                    ImageResource.RemoteFilePath(product.imageUrl, token = state.token)
                } else {
                    ImageResource.DrawableResource(product.imageResource ?: 0)
                }
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(start = 20.dp)
                        .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text(product.description)
                Text(product.price, style = MaterialTheme.typography.titleLarge)
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
                    products =
                        listOf(
                            Product(
                                description = "A delicious food",
                                imageResource = R.drawable.hawaiian,
                                name = "Hawaiian Pizza",
                                price = "$10.19"
                            ),
                            Product(
                                description = "Another food",
                                imageResource = R.drawable.meat_lovers,
                                name = "Meat Lovers Pizza",
                                price = "$12.98"
                            )
                        )
                )
        )
    }
}
