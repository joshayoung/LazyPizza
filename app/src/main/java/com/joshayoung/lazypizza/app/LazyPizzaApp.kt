package com.joshayoung.lazypizza.app

import android.app.Application
import com.joshayoung.lazypizza.app.data.di.appModule
import com.joshayoung.lazypizza.auth.domain.di.authModule
import com.joshayoung.lazypizza.cart.data.di.cartModule
import com.joshayoung.lazypizza.core.data.di.coreModule
import com.joshayoung.lazypizza.core.networking.JwtManager
import com.joshayoung.lazypizza.history.data.di.historyModule
import com.joshayoung.lazypizza.menu.data.di.menuModule
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
                appModule,
                cartModule,
                coreModule,
                historyModule,
                menuModule,
                authModule
            )
        }
    }
}
