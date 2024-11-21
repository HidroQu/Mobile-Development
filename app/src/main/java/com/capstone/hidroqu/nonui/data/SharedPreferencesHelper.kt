package com.capstone.hidroqu.nonui.data

import android.content.Context


class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }
}
