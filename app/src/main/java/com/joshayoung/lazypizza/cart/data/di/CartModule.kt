package com.joshayoung.lazypizza.cart.data.di

import androidx.room.Room
import com.joshayoung.lazypizza.cart.presentation.cart_list.CartListViewModel
import com.joshayoung.lazypizza.cart.presentation.checkout.CheckoutViewModel
import com.joshayoung.lazypizza.cart.presentation.confirmation.presentation.ConfirmationViewModel
import com.joshayoung.lazypizza.core.data.CartRepositoryImpl
import com.joshayoung.lazypizza.core.data.RoomLocalCartDataSource
import com.joshayoung.lazypizza.core.data.database.LazyPizzaDatabase
import com.joshayoung.lazypizza.core.data.network.AppWriteMenuRemoteDataSource
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.LocalCartDataSource
import com.joshayoung.lazypizza.core.domain.network.MenuRemoteDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var cartModule =
    module {
        viewModelOf(::CartListViewModel)
        viewModelOf(::CheckoutViewModel)
        viewModelOf(::ConfirmationViewModel)

        single {
            Room
                .databaseBuilder(
                    androidApplication(),
                    LazyPizzaDatabase::class.java,
                    LazyPizzaDatabase.DATABASE_NAME
                ).build()
        }

        single { get<LazyPizzaDatabase>().cardDao }
        single { get<LazyPizzaDatabase>().productDao }
        single { get<LazyPizzaDatabase>().toppingDao }

        singleOf(::RoomLocalCartDataSource).bind<LocalCartDataSource>()
        singleOf(::AppWriteMenuRemoteDataSource).bind<MenuRemoteDataSource>()

        single {
            CartRepositoryImpl(
                get()
            )
        }.bind<CartRepository>()
    }