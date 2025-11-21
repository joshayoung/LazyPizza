package com.joshayoung.lazypizza.order.data.di

import com.joshayoung.lazypizza.core.data.CartRepositoryImpl
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.order.data.OrderRepositoryImpl
import com.joshayoung.lazypizza.order.domain.OrderRepository
import com.joshayoung.lazypizza.order.presentation.order_history.OrderViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var orderModule =
    module {
        viewModelOf(::OrderViewModel)

        single {
            OrderRepositoryImpl(
                get(),
                get(),
                get()
            )
        }.bind<OrderRepository>()
    }
