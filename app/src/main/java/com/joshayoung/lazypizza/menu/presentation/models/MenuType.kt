package com.joshayoung.lazypizza.menu.presentation.models

enum class MenuType(
    val displayValue: String,
    val chipValue: String
) {
    Entree("PIZZA", "Pizza"),
    Beverage("DRINKS", "Drinks"),
    Sauce("SAUCES", "Sauces"),
    Dessert("ICE CREAM", "Ice Cream"),
    Unknown("", "")
}