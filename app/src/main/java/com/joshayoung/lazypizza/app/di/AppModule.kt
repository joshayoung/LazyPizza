package com.joshayoung.lazypizza.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.joshayoung.lazypizza.app.LazyPizzaApp
import com.joshayoung.lazypizza.app.MainViewModel
import com.joshayoung.lazypizza.cart.data.DataStorageCartRepository
import com.joshayoung.lazypizza.cart.domain.CartRepository
import com.joshayoung.lazypizza.core.data.AppWriteAuthRepository
import com.joshayoung.lazypizza.core.domain.AuthRepository
import com.joshayoung.lazypizza.core.networking.AppWriteClientProvider
import com.joshayoung.lazypizza.menu.domain.LoadProductsUseCase
import com.joshayoung.lazypizza.menu.presentation.details.DetailsScreenViewModel
import com.joshayoung.lazypizza.menu.presentation.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

fun createPreferencesDataStore(context: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("lazy_pizza_storage") },
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        corruptionHandler =
            ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            )
    )

var appModule =
    module {
        viewModelOf(::MainViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::DetailsScreenViewModel)

        single<CoroutineScope> {
            (androidApplication() as LazyPizzaApp).applicationScope
        }
        single { AppWriteAuthRepository(get()) }.bind<AuthRepository>()

        val cartQualifier = named("cartDataStore")

        single<DataStore<Preferences>>(qualifier = cartQualifier) {
            createPreferencesDataStore(
                get()
            )
        }

        single {
            DataStorageCartRepository(
                get(),
                get<DataStore<Preferences>>(qualifier = cartQualifier)
            )
        }.bind<CartRepository>()

        single {
            AppWriteClientProvider(get()).getInstance()
        }

        single {
            LoadProductsUseCase(get())
        }
    }
