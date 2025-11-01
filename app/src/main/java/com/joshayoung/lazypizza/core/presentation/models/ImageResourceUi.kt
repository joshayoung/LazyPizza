package com.joshayoung.lazypizza.core.presentation.models

sealed class ImageResourceUi {
    data class DrawableResourceUi(
        val id: Int?
    ) : ImageResourceUi()

    data class RemoteFilePath(
        val path: String?
    ) : ImageResourceUi()
}