package com.joshayoung.lazypizza.search.presentation.searchItems

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.search.ImageResource
import com.joshayoung.lazypizza.search.presentation.components.LazyImage
import com.joshayoung.lazypizza.ui.theme.GrayPhone
import com.joshayoung.lazypizza.ui.theme.PizzaLogo
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchItemsScreenRoot(viewModel: SearchItemsViewModel = koinViewModel()) {
    val images = viewModel.images.collectAsState(initial = emptyList())
    val myImages: MutableList<ImageResource> = mutableListOf()
    images.value.forEach {
        myImages.add(ImageResource.RemoteFilePath(it))
    }
    SearchItemsScreen(images = myImages)
}
@Composable
fun SearchItemsScreen(images: List<ImageResource>) {

    Text("Search Items")
    Scaffold(
        topBar = {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier =
                        Modifier
                            .windowInsetsPadding(WindowInsets.safeDrawing),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Image(
                        imageVector = PizzaLogo,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .padding(end = 10.dp),
                    )
                    Text(
                        text = "LazyPizza",
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = GrayPhone,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier =
                            Modifier
                                .padding(end = 10.dp),
                    )
                    Text(text = "+1 (555) 321-7890")
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding),
        ) {
            Image(
                painterResource(id = R.drawable.pizza_header),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier =
                    Modifier
                        .fillMaxWidth(),
            )
            BasicTextField(
                state =
                    TextFieldState(
                        initialText = "Search for delicious food…",
                    ),
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxSize(),
            ) {


                LazyColumn(
                    modifier =
                        Modifier
                            .size(400.dp)
                            .fillMaxSize(),
                ) {
                    items(images) { imageUrl ->
                        LazyImage(imageUrl)
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchItemsScreenPreview() {
    SearchItemsScreen(images = mutableListOf(
        ImageResource.DrawableResource(R.drawable.hawaiian),
        ImageResource.DrawableResource(R.drawable.meat_lovers)
    ))
}
