package com.joshayoung.lazypizza.di

import com.joshayoung.lazypizza.LazyPizzaApp
import com.joshayoung.lazypizza.MainViewModel
import com.joshayoung.lazypizza.core.data.AuthWriteRepository
import com.joshayoung.lazypizza.core.data.SharedPreferencesPreference
import com.joshayoung.lazypizza.core.domain.LazyPizzaPreference
import com.joshayoung.lazypizza.core.domain.LazyPizzaRepository
import com.joshayoung.lazypizza.search.presentation.details.DetailsScreenViewModel
import com.joshayoung.lazypizza.search.presentation.searchItems.SearchItemsViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var appModule =
    module {
        viewModelOf(::MainViewModel)
        viewModelOf(::SearchItemsViewModel)
        viewModelOf(::DetailsScreenViewModel)

        single<CoroutineScope> {
            (androidApplication() as LazyPizzaApp).applicationScope
        }
        single { AuthWriteRepository(get(), get()) }.bind<LazyPizzaRepository>()
        single { SharedPreferencesPreference(get()) }.bind<LazyPizzaPreference>()
    }
