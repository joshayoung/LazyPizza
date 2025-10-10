package com.joshayoung.lazypizza

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.domain.LazyPizzaAuth
import kotlinx.coroutines.launch

class MainViewModel(
    private var lazyPizzaAuth: LazyPizzaAuth
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            var loggedIn =
                lazyPizzaAuth.loginUser(
                    BuildConfig.AUTH_EMAIL,
                    BuildConfig.AUTH_PASSWORD
                )
            if (loggedIn) {
                state = state.copy(isLoading = false)
            }
        }
    }
}
