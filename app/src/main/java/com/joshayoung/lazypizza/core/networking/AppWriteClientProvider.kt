package com.joshayoung.lazypizza.core.networking

import android.content.Context
import com.joshayoung.lazypizza.BuildConfig
import io.appwrite.Client

class AppWriteClientProvider(
    private var context: Context
) {
    fun getInstance(): Client {
        val client =
            Client(context)
                .setEndpoint(BuildConfig.API_ENDPOINT)
                .setProject(BuildConfig.API_PROJECT_ID)

        return client
    }
}