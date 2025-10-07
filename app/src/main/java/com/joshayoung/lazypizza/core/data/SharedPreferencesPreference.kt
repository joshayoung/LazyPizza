package com.joshayoung.lazypizza.core.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.joshayoung.lazypizza.core.domain.LazyPizzaPreference

class SharedPreferencesPreference(
    private val context: Context
) : LazyPizzaPreference {
    override fun saveJwt(jwt: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(
                "lazy_pizza_storage",
                Context.MODE_PRIVATE
            )
        sharedPreferences.edit { putString("app_write_jwt_token", jwt) }
    }

    override fun getJwt(): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(
                "lazy_pizza_storage",
                Context.MODE_PRIVATE
            )
        return sharedPreferences.getString("app_write_jwt_token", null) ?: ""
    }
}