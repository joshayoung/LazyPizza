package com.joshayoung.lazypizza.history.domain.models

enum class OrderStatus(
    val displayValue: String
) {
    InProgress("In Progress"),
    Completed("Completed"),
    Unknown("Unknown")
}