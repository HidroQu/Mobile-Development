package com.capstone.hidroqu.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.BasicResponse
import com.capstone.hidroqu.nonui.data.LoginResponse
import com.capstone.hidroqu.nonui.data.MyPlantDetailResponse
import com.capstone.hidroqu.nonui.data.MyPlantDetailWrapper
import com.capstone.hidroqu.nonui.data.MyPlantResponse
import com.capstone.hidroqu.nonui.data.MyPlantResponseWrapper
import com.capstone.hidroqu.nonui.data.PlantResponse
import com.capstone.hidroqu.nonui.data.PlantResponseWrapper
import com.capstone.hidroqu.nonui.data.StorePlantRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPlantViewModel : ViewModel() {

    private val apiService: HidroQuApiService = HidroQuApiConfig.retrofit.create(HidroQuApiService::class.java)

    private val _myPlants = MutableStateFlow<List<MyPlantResponse>>(emptyList())
    val myPlants: StateFlow<List<MyPlantResponse>> get() = _myPlants

    private val _plantDetail = MutableStateFlow<MyPlantDetailResponse?>(null)
    val plantDetail: StateFlow<MyPlantDetailResponse?> get() = _plantDetail

    private val _plants = MutableStateFlow<List<PlantResponse>>(emptyList())
    val plants: StateFlow<List<PlantResponse>> get() = _plants

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun fetchMyPlants(token: String) {
        _isLoading.value = true
        apiService.getMyPlants("Bearer $token").enqueue(object : Callback<MyPlantResponseWrapper> {
            override fun onResponse(
                call: Call<MyPlantResponseWrapper>,
                response: Response<MyPlantResponseWrapper>
            ) {
                _isLoading.value = false
                Log.d("MyPlantViewModel", "API Response: ${response.body()}")

                if (response.isSuccessful) {
                    val plantList = response.body()?.data?.data ?: emptyList() // Mengambil data tanaman

                    if (plantList.isEmpty()) {
                        _errorMessage.value = "You have no plants yet."
                    } else {
                        _myPlants.value = plantList
                        _errorMessage.value = null
                    }
                } else {
                    _errorMessage.value = "Failed to load plants: ${response.message()}"
                    Log.e("MyPlantViewModel", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MyPlantResponseWrapper>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
                Log.e("MyPlantViewModel", "Failure: ${t.message}")
            }
        })
    }

    fun fetchMyPlantDetail(token: String, plantId: Int) {
        _isLoading.value = true
        apiService.getMyPlantDetail(("Bearer $token"), plantId).enqueue(object : Callback<MyPlantDetailWrapper> {
            override fun onResponse(
                call: Call<MyPlantDetailWrapper>,
                response: Response<MyPlantDetailWrapper>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val plantDetail = response.body()?.data
                    _plantDetail.value = plantDetail
                    Log.d("MyPlantViewModel", "Fetched plant detail: ${response.body()}")
                } else {
                    _errorMessage.value = "Error fetching plant details: ${response.message()}"
                    Log.e("MyPlantViewModel", "Error fetching details: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MyPlantDetailWrapper>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error fetching plant details: ${t.message}"
                Log.e("MyPlantViewModel", "Failure: ${t.message}")
            }
        })
    }



    // Store a new plant for the user
    fun storePlant(
        token: String,
        plantId: Int,
        plantingDate: String,
        notes: String?,
        onSuccess: (BasicResponse) -> Unit,
    ) {
        val request = StorePlantRequest(
            plant_id = plantId,
            planting_date = plantingDate,
            notes = notes
        )

        // Request to store plant
        apiService.storePlant("Bearer $token", request).enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    Log.d("MyPlantViewModel", "Plant stored successfully: ${response.body()}")
                    val responseBody = response.body()
                    responseBody?.let {
                        onSuccess(it)
                    }
                } else {
                    Log.e("MyPlantViewModel", "Failed to store plant: ${response.message()}")
                    _errorMessage.value = "Error storing plant: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.e("MyPlantViewModel", "Network error: ${t.message}")
                _errorMessage.value = "Network error: ${t.message}"
            }
        })

    }
    fun fetchPlants(token: String) {
        _isLoading.value = true

        // Menggunakan PlantResponseWrapper untuk mendapatkan data
        apiService.getPlants("Bearer $token").enqueue(object : Callback<PlantResponseWrapper> {
            override fun onResponse(call: Call<PlantResponseWrapper>, response: Response<PlantResponseWrapper>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    // Ambil daftar tanaman dari response.body()?.data
                    _plants.value = response.body()?.data ?: emptyList()
                } else {
                    _errorMessage.value = "Failed to load plants: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<PlantResponseWrapper>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }
}
