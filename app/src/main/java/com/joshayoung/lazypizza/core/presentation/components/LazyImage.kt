package com.joshayoung.lazypizza.core.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.networking.JwtManager
import com.joshayoung.lazypizza.core.presentation.models.ImageResource

@Composable
fun LazyImage(
    imageResource: ImageResource,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val token = JwtManager.token
    when (imageResource) {
        is ImageResource.DrawableResource -> {
            Image(
                painterResource(id = imageResource.id),
                contentDescription = null,
                modifier = modifier
            )
        }
        is ImageResource.RemoteFilePath -> {
            if (token != null) {
                val headers =
                    NetworkHeaders
                        .Builder()
                        .set(BuildConfig.AUTH_HEADER, token)
                        .build()
                val request =
                    ImageRequest
                        .Builder(context)
                        .data(imageResource.path)
                        .httpHeaders(headers)
                        .build()
                AsyncImage(
                    modifier = modifier,
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
                AsyncImage(
                    modifier = modifier,
                    model = imageResource.path,
                    contentDescription = null
                )
            }
        }
    }
}