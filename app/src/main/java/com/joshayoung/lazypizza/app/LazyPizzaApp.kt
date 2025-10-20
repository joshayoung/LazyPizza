package com.joshayoung.lazypizza.app

import android.app.Application
import com.joshayoung.lazypizza.app.di.appModule
import com.joshayoung.lazypizza.core.networking.JwtManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class LazyPizzaApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        JwtManager.init(this)

        startKoin {
            androidContext(this@LazyPizzaApp)
            androidLogger(Level.DEBUG)
            modules(
                appModule
            )
        }
    }
}
