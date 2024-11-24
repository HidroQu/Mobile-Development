package com.capstone.hidroqu.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.PlantResponseWrapper
import com.capstone.hidroqu.nonui.data.ProfileResponse
import com.capstone.hidroqu.nonui.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel: ViewModel() {
    private val apiService: HidroQuApiService = HidroQuApiConfig.retrofit.create(HidroQuApiService::class.java)
    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> get() = _userData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun fetchUserProfile(token: String) {
        _isLoading.value = true

        // Menggunakan PlantResponseWrapper untuk mendapatkan data
        apiService.getUserProfile("Bearer $token").enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d("ProfileViewModel", "Response: ${response.body()}")
                    _userData.value = response.body()?.data
                } else {
                    Log.e("ProfileViewModel", "Error: ${response.errorBody()?.string()}")
                    _errorMessage.value = "Failed to load profile: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }
}