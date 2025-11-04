package com.joshayoung.lazypizza.auth.domain.di

import android.app.Activity
import com.joshayoung.lazypizza.auth.presentation.FirebaseAuthenticator
import com.joshayoung.lazypizza.auth.presentation.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import kotlin.coroutines.EmptyCoroutineContext.get

val authModule =
    module {
        factory { (activity: Activity) ->
            FirebaseAuthenticator(activity)
        }
        // TODO: Correct this, it will cause a memory leak:
        viewModel { (activity: Activity) -> LoginViewModel(get { parametersOf(activity) }) }
    }