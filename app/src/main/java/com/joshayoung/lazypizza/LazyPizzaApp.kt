package com.joshayoung.lazypizza

import android.app.Application
import com.joshayoung.lazypizza.di.appModule
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

        startKoin {
            androidContext(this@LazyPizzaApp)
            androidLogger(Level.DEBUG)
            modules(
                appModule,
            )
        }
    }
}