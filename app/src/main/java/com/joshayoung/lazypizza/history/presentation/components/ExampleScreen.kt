package com.joshayoung.lazypizza.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Place(
    val name: String,
    val foods: List<String>
)

@Composable
fun ExampleList(places: List<Place>) {
    LazyVerticalStaggeredGrid(
        state = rememberLazyStaggeredGridState(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        columns = StaggeredGridCells.Fixed(2)
    ) {
        items(places) { place ->
            ExampleCard(place)
        }
    }
}

@Composable
fun ExampleCard(
    place: Place,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier
                .background(Color.LightGray)
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier =
                Modifier
                    .background(Color.Red)
        ) {
            Box() {
                Column {
                    place.foods.forEach { food ->
                        Text(food)
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Text("TOP")
            Text("BOTTOM:")
        }
    }
}

val all =
    listOf(
        Place(
            "one",
            foods =
                listOf(
                    "first food",
                    "second food",
                    "third food",
                    "fourth food",
                    "fifth food",
                    "sixth food",
                    "seventh food"
                )
        ),
        Place(
            "two",
            foods =
                listOf(
                    "first food",
                    "second food"
                )
        )
    )

@Preview(showBackground = true)
@Composable
fun ExampleListPreview() {
    ExampleList(all)
}
