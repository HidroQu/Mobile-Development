package com.capstone.hidroqu.nonui.data
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        private val USER_ID = intPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val TOKEN = stringPreferencesKey("token")
    }

    val userId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }

    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }

    val userEmail: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }

    val token: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN]
    }

    suspend fun saveUserData(userId: Int, userName: String, userEmail: String, token: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_NAME] = userName
            preferences[USER_EMAIL] = userEmail
            preferences[TOKEN] = token
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}