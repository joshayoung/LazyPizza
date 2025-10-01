package com.joshayoung.lazypizza.di

import com.joshayoung.lazypizza.LazyPizzaApp
import com.joshayoung.lazypizza.MainViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

var appModule =
    module {
        viewModelOf(::MainViewModel)

        single<CoroutineScope> {
            (androidApplication() as LazyPizzaApp).applicationScope
        }
    }