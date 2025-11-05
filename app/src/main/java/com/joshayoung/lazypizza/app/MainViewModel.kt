package com.joshayoung.lazypizza.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.auth.domain.AuthState
import com.joshayoung.lazypizza.core.domain.AuthRepository
import com.joshayoung.lazypizza.core.domain.CartRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private var authRepository: AuthRepository,
    private var cartRepository: CartRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    val authState: Flow<AuthState> =
        callbackFlow {
            val listener =
                // TODO: Should this be injected?
                FirebaseAuth.AuthStateListener { firebaseAuth ->
                    val user = firebaseAuth.currentUser
                    trySend(AuthState(user != null, user?.uid))
                }

            firebaseAuth.addAuthStateListener(listener)

            awaitClose { firebaseAuth.removeAuthStateListener(listener) }
        }

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
            cartRepository.getNumberProductsInCart(1).collectLatest { count ->
                state = state.copy(cartItems = count)
            }
        }
    }
}
