package com.joshayoung.lazypizza.menu.data.di

import com.google.firebase.auth.FirebaseAuth
import com.joshayoung.lazypizza.menu.domain.LoadProductsUseCase
import com.joshayoung.lazypizza.menu.presentation.details.DetailViewModel
import com.joshayoung.lazypizza.menu.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

var menuModule =
    module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::DetailViewModel)

        single {
            LoadProductsUseCase(get(), get())
        }

        single {
            FirebaseAuth.getInstance()
        }
    }
