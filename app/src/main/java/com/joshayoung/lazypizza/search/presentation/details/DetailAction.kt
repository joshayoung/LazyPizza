package com.joshayoung.lazypizza.search.presentation.details

import java.math.BigDecimal

sealed interface DetailAction {
    data class IncrementPrice(
        var price: BigDecimal
    ) : DetailAction

    data class DecrementPrice(
        var price: BigDecimal
    ) : DetailAction
}