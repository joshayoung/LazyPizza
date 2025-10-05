package com.joshayoung.lazypizza.di

import com.joshayoung.lazypizza.LazyPizzaApp
import com.joshayoung.lazypizza.MainViewModel
import com.joshayoung.lazypizza.search.data.utils.AppWriteStorage
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaStorage
import com.joshayoung.lazypizza.search.presentation.searchItems.SearchItemsViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
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
        single { AppWriteStorage(get()) }.bind<LazyPizzaStorage>() }
