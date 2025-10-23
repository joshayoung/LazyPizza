package com.joshayoung.lazypizza.menu.data.di

import com.joshayoung.lazypizza.menu.domain.LoadProductsUseCase
import com.joshayoung.lazypizza.menu.presentation.details.DetailsScreenViewModel
import com.joshayoung.lazypizza.menu.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

var menuModule =
    module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::DetailsScreenViewModel)

        single {
            LoadProductsUseCase(get(), get())
        }
    }
