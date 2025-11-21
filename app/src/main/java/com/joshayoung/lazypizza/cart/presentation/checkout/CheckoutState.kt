package com.joshayoung.lazypizza.cart.presentation.checkout

import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import java.math.BigDecimal

data class CheckoutState(
    val items: List<InCartItemUi> = emptyList(),
    val isLoadingCart: Boolean = true,
    val cartItems: Int = 0,
    val recommendedAddOns: List<ProductUi> = emptyList(),
    val checkoutPrice: BigDecimal = BigDecimal(0.0),
    val earliestTimeSet: Boolean = true,
    val datePickerOpen: Boolean = false,
    val timePickerOpen: Boolean = false,
    val futureDeliveryTimeSet: Boolean = false,
    val futureDeliveryDateSelected: Boolean = false,
    val timeError: String = "",
    val pickupTime: String = "00:00",
    val date: Long? = null,
    val hour: Int? = null,
    val minute: Int? = null,
    val orderInProgress: Boolean = false
) {
    val customScheduleActive: Boolean
        get() = futureDeliveryTimeSet || futureDeliveryDateSelected

    val earliestAvailableActive: Boolean
        get() = !futureDeliveryTimeSet && !futureDeliveryDateSelected
}
