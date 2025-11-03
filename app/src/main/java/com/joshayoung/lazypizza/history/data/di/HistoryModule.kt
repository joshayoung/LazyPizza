package com.joshayoung.lazypizza.history.data.di

import com.joshayoung.lazypizza.history.data.HistoryRepositoryImpl
import com.joshayoung.lazypizza.history.domain.HistoryRepository
import com.joshayoung.lazypizza.history.presentation.order_history.HistoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var historyModule =
    module {
        viewModelOf(::HistoryViewModel)

        single {
            HistoryRepositoryImpl()
        }.bind<HistoryRepository>()
    }
