package com.joshayoung.lazypizza.order.data.di

import com.joshayoung.lazypizza.order.presentation.order_history.OrderViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

var orderModule =
    module {
        viewModelOf(::OrderViewModel)
    }
