package com.joshayoung.lazypizza.di

import com.joshayoung.lazypizza.LazyPizzaApp
import com.joshayoung.lazypizza.MainViewModel
import com.joshayoung.lazypizza.search.data.utils.AppWriteAuth
import com.joshayoung.lazypizza.search.data.utils.AppWriteDatabase
import com.joshayoung.lazypizza.search.data.utils.AppWriteStorage
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaAuth
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaDatabase
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaStorage
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

        single<CoroutineScope> {
            (androidApplication() as LazyPizzaApp).applicationScope
        }
        single { AppWriteStorage(get()) }.bind<LazyPizzaStorage>()
        single { AppWriteDatabase(get()) }.bind<LazyPizzaDatabase>()
        single { AppWriteAuth(get()) }.bind<LazyPizzaAuth>()
    }
