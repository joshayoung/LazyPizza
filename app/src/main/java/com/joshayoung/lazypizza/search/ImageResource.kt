package com.joshayoung.lazypizza.search

sealed class ImageResource {
    data class DrawableResource(
        val id: Int,
    ) : ImageResource()

    data class RemoteFilePath(
        val path: String,
    ) : ImageResource()
}
