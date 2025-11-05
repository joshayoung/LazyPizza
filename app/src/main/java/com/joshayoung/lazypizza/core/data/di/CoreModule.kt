package com.joshayoung.lazypizza.core.data.di

import com.google.firebase.auth.FirebaseAuth
import com.joshayoung.lazypizza.core.data.AppWriteAuthRepository
import com.joshayoung.lazypizza.core.domain.AuthRepository
import com.joshayoung.lazypizza.core.networking.AppWriteClientProvider
import org.koin.dsl.bind
import org.koin.dsl.module

var coreModule =
    module {
        single { AppWriteAuthRepository(get()) }.bind<AuthRepository>()

        single {
            AppWriteClientProvider(get()).getInstance()
        }

        single {
            FirebaseAuth.getInstance()
        }
    }
