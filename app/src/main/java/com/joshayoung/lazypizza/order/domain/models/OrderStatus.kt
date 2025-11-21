package com.joshayoung.lazypizza.order.domain.models

enum class OrderStatus(
    val displayValue: String
) {
    InProgress("In Progress"),
    Completed("Completed"),
    Unknown("Unknown")
}