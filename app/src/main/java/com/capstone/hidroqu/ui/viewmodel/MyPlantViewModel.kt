package com.capstone.hidroqu.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.BasicResponse
import com.capstone.hidroqu.nonui.data.MyPlantDetailResponse
import com.capstone.hidroqu.nonui.data.MyPlantResponse
import com.capstone.hidroqu.nonui.data.MyPlantResponseWrapper
import com.capstone.hidroqu.nonui.data.PlantResponseWrapper
import com.capstone.hidroqu.nonui.data.StorePlantRequest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPlantViewModel : ViewModel() {

    private val apiService: HidroQuApiService = HidroQuApiConfig.retrofit.create(HidroQuApiService::class.java)

    private val _myPlants = MutableLiveData<List<MyPlantResponse>>()
    val myPlants: LiveData<List<MyPlantResponse>> get() = _myPlants

    private val _plantDetail = MutableLiveData<MyPlantDetailResponse>()
    val plantDetail: LiveData<MyPlantDetailResponse> get() = _plantDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun storePlant(token: String, plantId: Int, plantingDate: String, notes: String?) {
        val request = StorePlantRequest(
            plant_id = plantId,
            planting_date = plantingDate,
            notes = notes
        )

        // Lakukan request ke API untuk menyimpan tanaman
        apiService.storePlant("Bearer $token", request).enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    // Berhasil menyimpan, lakukan tindakan seperti navigasi atau tampilkan pesan
                } else {
                    // Tangani kesalahan
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                // Tangani kegagalan
            }
        })
    }

    fun fetchMyPlantDetail(token: String, plantId: Int) {
        _isLoading.value = true
        apiService.getMyPlantDetail(plantId)
            .enqueue(object : Callback<MyPlantDetailResponse> {
                override fun onResponse(
                    call: Call<MyPlantDetailResponse>,
                    response: Response<MyPlantDetailResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _plantDetail.value = response.body()
                    } else {
                        _errorMessage.value = "Error fetching plant details"
                    }
                }

                override fun onFailure(call: Call<MyPlantDetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Network error: ${t.message}"
                }
            })
    }

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

}
