package com.joshayoung.lazypizza.search.presentation.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DetailsScreenRoot() {
    DetailsScreen()
}

@Composable
fun DetailsScreen() {
    Text(text = "Details…")
}

@Composable
@Preview
fun DetailsScreenPreview() {
    DetailsScreen()
}