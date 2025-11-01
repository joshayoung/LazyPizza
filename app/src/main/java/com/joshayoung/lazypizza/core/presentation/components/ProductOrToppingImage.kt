package com.joshayoung.lazypizza.core.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.networking.JwtManager
import com.joshayoung.lazypizza.core.presentation.models.ImageResourceUi

// TODO: Move preview and debug check here:
@Composable
fun ProductOrToppingImage(
    imageResource: Int?,
    remoteImage: String?,
    modifier: Modifier = Modifier
) {
    val inPreviewMode = LocalInspectionMode.current
    val context = LocalContext.current

    val imageResourceUi =
        if (inPreviewMode) {
            ImageResourceUi.DrawableResourceUi(imageResource)
        } else {
            ImageResourceUi.RemoteFilePath(remoteImage)
        }

    var token: String? = null
    if (!inPreviewMode) {
        token = JwtManager.token
    }

    when (imageResourceUi) {
        is ImageResourceUi.DrawableResourceUi -> {
            imageResourceUi.id?.let { id ->
                Image(
                    painterResource(id = id),
                    contentDescription = null,
                    modifier = modifier
                )
            }
        }
        is ImageResourceUi.RemoteFilePath -> {
            if (token != null) {
                val headers =
                    NetworkHeaders
                        .Builder()
                        .set(BuildConfig.AUTH_HEADER, token)
                        .build()
                val request =
                    ImageRequest
                        .Builder(context)
                        .data(imageResourceUi.path)
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
                    model = imageResourceUi.path,
                    contentDescription = null
                )
            }
        }
    }
}