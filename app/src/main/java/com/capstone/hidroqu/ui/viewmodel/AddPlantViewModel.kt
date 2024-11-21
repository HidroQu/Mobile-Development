package com.capstone.hidroqu.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.PlantResponse
import com.capstone.hidroqu.nonui.data.PlantResponseWrapper
import com.capstone.hidroqu.nonui.data.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPlantViewModel(context: Context) : ViewModel() {
    private val apiService: HidroQuApiService = HidroQuApiConfig.retrofit.create(HidroQuApiService::class.java)
    private val sharedPreferencesHelper = SharedPreferencesHelper(context)

    private val _plants = MutableLiveData<List<PlantResponse>>()
    val plants: LiveData<List<PlantResponse>> get() = _plants

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchPlants() {
        _isLoading.value = true

        // Ambil token dari SharedPreferences
        val token = sharedPreferencesHelper.getToken()
        Log.d("AddPlantViewModel", "Token: $token")

        // Menggunakan PlantResponseWrapper untuk mendapatkan data
        apiService.getPlants("Bearer $token").enqueue(object : Callback<PlantResponseWrapper> {
            override fun onResponse(call: Call<PlantResponseWrapper>, response: Response<PlantResponseWrapper>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d("AddPlantViewModel", "Plants fetched successfully: ${response.body()}")
                    // Ambil daftar tanaman dari response.body()?.data
                    _plants.value = response.body()?.data
                } else {
                    _errorMessage.value = "Failed to load plants: ${response.message()}"
                    Log.e("AddPlantViewModel", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PlantResponseWrapper>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
                Log.e("AddPlantViewModel", "Failure: ${t.message}")
            }
        })
    }
}
