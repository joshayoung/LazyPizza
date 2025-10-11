package com.joshayoung.lazypizza.core.networking

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object JwtManager {
    private const val PREFERENCES_NAME = "lazy_pizza"
    private const val JWT_KEY = "lazy_jwt"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = sharedPreferences.getString(JWT_KEY, null)
        set(value) {
            sharedPreferences.edit { putString(JWT_KEY, value) }
        }
}