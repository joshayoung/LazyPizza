package com.joshayoung.lazypizza.search.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.search.ImageResource

@Composable
fun LazyImage(imageResource: ImageResource) {
    val context = LocalContext.current
    when (imageResource) {
        is ImageResource.DrawableResource -> {
            Image(painterResource(id = imageResource.id), contentDescription = null)
        }
        is ImageResource.RemoteFilePath -> {
            if (imageResource.token != null) {
                val headers =
                    NetworkHeaders
                        .Builder()
                        .set(BuildConfig.AUTH_HEADER, imageResource.token)
                        .build()
                val request =
                    ImageRequest
                        .Builder(context)
                        .data(imageResource.path)
                        .httpHeaders(headers)
                        .build()
                AsyncImage(
                    model = request,
                    contentDescription = null,
                    onError = {
                        Log.e(
                            "ee",
                            it.result.throwable.message
                                .toString()
                        )
                    }
                )
            } else {
                AsyncImage(model = imageResource.path, contentDescription = null)
            }
        }
    }
}