package com.joshayoung.lazypizza.di

import com.joshayoung.lazypizza.LazyPizzaApp
import com.joshayoung.lazypizza.MainViewModel
import com.joshayoung.lazypizza.core.data.AuthWriteRepository
import com.joshayoung.lazypizza.core.domain.LazyPizzaRepository
import com.joshayoung.lazypizza.core.networking.AuthWriteClientProvider
import com.joshayoung.lazypizza.search.presentation.details.DetailsScreenViewModel
import com.joshayoung.lazypizza.search.presentation.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var appModule =
    module {
        viewModelOf(::MainViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::DetailsScreenViewModel)

        single<CoroutineScope> {
            (androidApplication() as LazyPizzaApp).applicationScope
        }
        single { AuthWriteRepository(get()) }.bind<LazyPizzaRepository>()

        single {
            AuthWriteClientProvider(get()).getInstance()
        }
    }
