package com.joshayoung.lazypizza.search.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.joshayoung.lazypizza.search.ImageResource

@Composable
fun LazyImage(imageResource: ImageResource) {
    when (imageResource) {
        is ImageResource.DrawableResource -> {
            Image(painterResource(id = imageResource.id), contentDescription = null)
        }
        is ImageResource.RemoteFilePath -> {
            AsyncImage(model = imageResource.path, contentDescription = null)
        }
    }
}
