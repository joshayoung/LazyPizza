package com.joshayoung.lazypizza.cart.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.joshayoung.lazypizza.cart.data.CartLocalDataSource
import com.joshayoung.lazypizza.cart.data.DataStorageCartRepository
import com.joshayoung.lazypizza.cart.data.database.CartDatabase
import com.joshayoung.lazypizza.cart.domain.CartRepository
import com.joshayoung.lazypizza.cart.domain.LocalDataSource
import com.joshayoung.lazypizza.cart.presentation.CartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
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

var cartModule =
    module {
        viewModelOf(::CartViewModel)

        val cartQualifier = named("cartDataStore")

        single<DataStore<Preferences>>(qualifier = cartQualifier) {
            createPreferencesDataStore(
                get()
            )
        }

        single {
            Room
                .databaseBuilder(
                    androidApplication(),
                    CartDatabase::class.java,
                    CartDatabase.DATABASE_NAME
                ).build()
        }

        single { get<CartDatabase>().cardDao }

        singleOf(::CartLocalDataSource).bind<LocalDataSource>()

        single {
            DataStorageCartRepository(
                get(),
                get<DataStore<Preferences>>(qualifier = cartQualifier)
            )
        }.bind<CartRepository>()
    }