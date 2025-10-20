package com.joshayoung.lazypizza.cart.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.joshayoung.lazypizza.cart.domain.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private object CartValues {
    val CART_ITEMS = stringPreferencesKey("cart_items")
}

class DataStorageCartRepository(
    private val dataStore: DataStore<Preferences>
) : CartRepository {
    override suspend fun addToCart(item: Int) {
        dataStore.edit { preferences ->
            val current = preferences[CartValues.CART_ITEMS]
            var count = current?.toInt() ?: 0
            val newCount = ++count
            preferences[CartValues.CART_ITEMS] = newCount.toString()
        }
    }

    override suspend fun removeFromCart() {
        dataStore.edit { preferences ->
            val current = preferences[CartValues.CART_ITEMS]
            var count = current?.toInt() ?: 0
            val newCount = --count
            preferences[CartValues.CART_ITEMS] = newCount.toString()
        }
    }

    override fun getCartData(): Flow<String?> =
        dataStore.data.map { preferences ->

            preferences[CartValues.CART_ITEMS]
        }
}