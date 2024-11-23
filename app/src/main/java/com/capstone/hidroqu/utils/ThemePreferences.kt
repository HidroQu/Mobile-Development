package com.capstone.hidroqu.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemePreference(private val context: Context) {
    private val themeKey = stringPreferencesKey("theme_setting")

    val getThemeSetting: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[themeKey] ?: "system"
        }

    suspend fun saveThemeSetting(themeName: String) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = themeName
        }
    }
}