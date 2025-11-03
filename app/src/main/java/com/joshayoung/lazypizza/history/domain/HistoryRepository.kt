package com.joshayoung.lazypizza.history.domain

import com.joshayoung.lazypizza.history.domain.models.Order

interface HistoryRepository {
    fun getOrdersFor(user: String): List<Order>
}