package com.joshayoung.lazypizza.app.data.di

import com.joshayoung.lazypizza.app.LazyPizzaApp
import com.joshayoung.lazypizza.app.MainViewModel
import com.joshayoung.lazypizza.app.data.AppWriteAuthRepository
import com.joshayoung.lazypizza.app.domain.AuthRepository
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var appModule =
    module {
        single<CoroutineScope> {
            (androidApplication() as LazyPizzaApp).applicationScope
        }
        viewModelOf(::MainViewModel)
        single { AppWriteAuthRepository(get()) }.bind<AuthRepository>()
    }
