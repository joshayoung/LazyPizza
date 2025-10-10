package com.joshayoung.lazypizza.di

import com.joshayoung.lazypizza.LazyPizzaApp
import com.joshayoung.lazypizza.MainViewModel
import com.joshayoung.lazypizza.core.data.SharedPreferencesPreference
import com.joshayoung.lazypizza.core.domain.LazyPizzaAuth
import com.joshayoung.lazypizza.core.domain.LazyPizzaDatabase
import com.joshayoung.lazypizza.core.domain.LazyPizzaPreference
import com.joshayoung.lazypizza.core.domain.LazyPizzaStorage
import com.joshayoung.lazypizza.search.data.utils.AppWriteAuth
import com.joshayoung.lazypizza.search.data.utils.AppWriteDatabase
import com.joshayoung.lazypizza.search.data.utils.AppWriteStorage
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
        single { AppWriteStorage(get()) }.bind<LazyPizzaStorage>()
        single { AppWriteDatabase(get()) }.bind<LazyPizzaDatabase>()
        single { AppWriteAuth(get(), get()) }.bind<LazyPizzaAuth>()
        single { SharedPreferencesPreference(get()) }.bind<LazyPizzaPreference>()
    }
