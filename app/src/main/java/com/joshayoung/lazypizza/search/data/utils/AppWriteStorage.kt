package com.joshayoung.lazypizza.search.data.utils

import android.content.Context
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaStorage
import io.appwrite.Client
import io.appwrite.services.Storage

class AppWriteStorage(
    private var context: Context
) : LazyPizzaStorage {
    override suspend fun getAllFiles(): List<String> {
        val client =
            Client(this.context)
                .setEndpoint(BuildConfig.API_ENDPOINT)
                .setProject(BuildConfig.API_PROJECT_ID)
        val storage = Storage(client)
        val t = storage.listFiles(BuildConfig.BUCKET_ID)
        val all = t.files.map { imagePath(it.id) }

        return all
    }

    private fun imagePath(id: String): String {
        val t =
            """
            ${BuildConfig.API_ENDPOINT}/
            storage/buckets/
            ${BuildConfig.BUCKET_ID}
            /files/
            $id
            /view?project=${BuildConfig.API_PROJECT_ID}
            """.trimIndent()

        return t.replace("\n", "").replace(Regex("\\s+"), "").trim()
    }
}