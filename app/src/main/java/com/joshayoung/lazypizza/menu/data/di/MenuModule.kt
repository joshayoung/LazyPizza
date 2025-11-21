package com.joshayoung.lazypizza.menu.data.di

import com.google.firebase.auth.FirebaseAuth
import com.joshayoung.lazypizza.core.data.CartRepositoryImpl
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.menu.data.MenuRepositoryImpl
import com.joshayoung.lazypizza.menu.domain.MenuRepository
import com.joshayoung.lazypizza.menu.presentation.details.DetailViewModel
import com.joshayoung.lazypizza.menu.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var menuModule =
    module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::DetailViewModel)

        single {
            FirebaseAuth.getInstance()
        }

        single {
            MenuRepositoryImpl(
                get(),
                get()
            )
        }.bind<MenuRepository>()
    }
