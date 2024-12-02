package com.capstone.hidroqu.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.MyPlantResponse
import com.capstone.hidroqu.nonui.data.MyPlantResponseWrapper
import com.capstone.hidroqu.nonui.data.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    // StateFlow untuk nama pengguna
    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> get() = _userName

    // StateFlow untuk loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        loadUserData()
    }

    // Memuat data pengguna
    private fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            userPreferences.userName
                .catch { e ->
                    _userName.value = "User"
                }
                .collect { name ->
                    _userName.value = name ?: "User"
                    _isLoading.value = false
                }
        }
    }

}
