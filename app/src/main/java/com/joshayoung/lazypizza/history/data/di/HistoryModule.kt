package com.joshayoung.lazypizza.history.data.di

import com.joshayoung.lazypizza.history.presentation.order_history.HistoryViewModel
import com.joshayoung.lazypizza.menu.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

var historyModule =
    module {
        viewModelOf(::HistoryViewModel)
    }
