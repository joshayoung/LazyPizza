package com.joshayoung.lazypizza.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.AuthRepository
import com.joshayoung.lazypizza.core.domain.CartRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private var authRepository: AuthRepository,
    private var cartRepository: CartRepository
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            var loggedIn =
                authRepository.login(
                    BuildConfig.AUTH_EMAIL,
                    BuildConfig.AUTH_PASSWORD
                )
            if (loggedIn) {
                // TODO: Pass in something identifying the user and do not hard-code this cartId:
                cartRepository.createCartForUser(1, "theUser")
                state = state.copy(isLoading = false)
            }
        }
    }
}
