package com.joshayoung.lazypizza.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.app.domain.AuthRepository
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.FirebaseAuthUiClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private var authRepository: AuthRepository,
    private var cartRepository: CartRepository
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    val authFlow = FirebaseAuthUiClient().appWriteState

    init {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            // NOTE: This login if for creating a authenticated session for AppWrite.
            // This is separate from the actual firebase SMS login and not visible to the user.
            // Because this requires internet to login, the app will not navigate past the splash
            // page without a internet connection.
            var loggedIn =
                authRepository.login(
                    BuildConfig.AUTH_EMAIL,
                    BuildConfig.AUTH_PASSWORD
                )

            if (loggedIn) {
                cartRepository.createCartForUser(
                    1,
                    BuildConfig.GUEST_USER
                )
                state = state.copy(isLoading = false)
            }
            cartRepository.getNumberProductsInCart(1).collectLatest { count ->
                state = state.copy(cartItems = count)
            }
        }
    }
}
