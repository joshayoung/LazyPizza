package com.joshayoung.lazypizza.order.presentation.models

enum class OrderStatusUi(
    val displayValue: String
) {
    InProgress("In Progress"),
    Completed("Completed"),
    Unknown("Unknown")
}