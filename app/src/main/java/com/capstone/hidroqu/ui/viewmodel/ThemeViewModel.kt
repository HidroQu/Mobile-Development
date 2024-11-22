package com.capstone.hidroqu.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val themePreference = ThemePreference(application)
    private val _themeMode = MutableStateFlow("system")
    val themeMode: StateFlow<String> = _themeMode

    init {
        viewModelScope.launch {
            _themeMode.value = themePreference.getThemeSetting.first()
        }
    }

    fun setTheme(newTheme: String) {
        viewModelScope.launch {
            themePreference.saveThemeSetting(newTheme)
            _themeMode.value = newTheme
        }
    }
}
