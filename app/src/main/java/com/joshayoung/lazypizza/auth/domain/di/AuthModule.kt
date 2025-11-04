package com.joshayoung.lazypizza.auth.domain.di

import com.joshayoung.lazypizza.auth.presentation.LoginViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule =
    module {
        viewModelOf(::LoginViewModel)
    }