package com.joshayoung.lazypizza.auth.data.di

import com.joshayoung.lazypizza.app.data.AppWriteAuthRepository
import com.joshayoung.lazypizza.app.domain.AuthRepository
import com.joshayoung.lazypizza.auth.presentation.LoginViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule =
    module {
        viewModelOf(::LoginViewModel)
    }