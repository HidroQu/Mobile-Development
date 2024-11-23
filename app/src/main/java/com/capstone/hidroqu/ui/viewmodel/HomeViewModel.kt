package com.capstone.hidroqu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> get() = _userName

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true // Set loading state to true
            userPreferences.userName
                .catch { e ->
                    // Handle error (e.g., log it)
                    _userName.value = "User"
                }
                .collect { name ->
                    _userName.value = name ?: "User"
                    _isLoading.value = false // Set loading state to false
                }
        }
    }
}