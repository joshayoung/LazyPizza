package com.joshayoung.lazypizza.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.domain.AuthRepository
import com.joshayoung.lazypizza.core.domain.models.CartEntity
import kotlinx.coroutines.launch

class MainViewModel(
    private var authRepository: AuthRepository,
    private var cartDao: CartDao
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
                state = state.copy(isLoading = false)
            }
            // TODO: Cleanup this:
            // this is being recreated every time
//            cartDao.addCart(
//                CartEntity(
//                    1,
//                    "Pizza Orders"
//                )
//            )
        }
    }
}
