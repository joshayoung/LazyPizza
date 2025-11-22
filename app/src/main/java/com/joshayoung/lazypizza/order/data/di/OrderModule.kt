package com.joshayoung.lazypizza.order.data.di

import com.joshayoung.lazypizza.order.data.OrderProcessorImpl
import com.joshayoung.lazypizza.order.data.OrderRepositoryImpl
import com.joshayoung.lazypizza.order.domain.OrderProcessor
import com.joshayoung.lazypizza.order.domain.OrderRepository
import com.joshayoung.lazypizza.order.presentation.order_history.OrderHistoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var orderModule =
    module {
        viewModelOf(::OrderHistoryViewModel)

        single {
            OrderRepositoryImpl(
                get()
            )
        }.bind<OrderRepository>()

        single {
            OrderProcessorImpl(
                get(),
                get()
            )
        }.bind<OrderProcessor>()
    }
