package com.joshayoung.lazypizza.menu.presentation.models

enum class MenuType(
    val displayValue: String
) {
    Entree("PIZZA"),
    Dessert("ICE CREAM"),
    Beverage("DRINKS"),
    Sauce("SAUCES"),
    Unknown("")
}