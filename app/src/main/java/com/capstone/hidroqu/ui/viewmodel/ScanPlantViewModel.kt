package com.capstone.hidroqu.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiPredictConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.BasicResponse
import com.capstone.hidroqu.nonui.data.DiagnosticInfo
import com.capstone.hidroqu.nonui.data.NutrientPredictionResponse
import com.capstone.hidroqu.nonui.data.PlantPredictionResponse
import com.capstone.hidroqu.nonui.data.StorePlantRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanPlantViewModel: ViewModel(){
    private val apiService : HidroQuApiService =
        HidroQuApiPredictConfig.getApiService()

    private val _plantPrediction = MutableStateFlow<PlantPredictionResponse?>(null)
    val plantPrediction: StateFlow<PlantPredictionResponse?> get() = _plantPrediction

    private val _nutrientPrediction = MutableStateFlow<NutrientPredictionResponse?>(null)
    val nutrientPrediction: StateFlow<NutrientPredictionResponse?> get() = _nutrientPrediction

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun predictPlant(
        token: String,
        imageUri: Uri?,
        context: Context,
        onSuccess: (PlantPredictionResponse) -> Unit
    ) {
        _isLoading.value = true
        val imagePart: MultipartBody.Part? = imageUri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val byteArray = inputStream?.readBytes()
            Log.d("ScanPlantViewModel", "Image size: ${byteArray?.size} bytes")
            val requestFile = byteArray?.toRequestBody("image/jpeg".toMediaTypeOrNull())
            requestFile?.let { file ->
                MultipartBody.Part.createFormData("plant_img", "filename.jpg", file)
            }
        }
        viewModelScope.launch {
            apiService.predictPlant(
                "Bearer $token",
                imagePart
            ).enqueue(object : Callback<PlantPredictionResponse> {
                    override fun onResponse(
                        call: Call<PlantPredictionResponse>,
                        response: Response<PlantPredictionResponse>
                    ) {
                        _isLoading.value = false
                        if (response.isSuccessful) {
                            response.body()?.let {
                                _plantPrediction.value = it
                                onSuccess(it)
                            }
                        } else {
                            _errorMessage.value =
                                "Failed to make plant prediction: ${response.message()}"
                            Log.e("MyPlantViewModel", "Error: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<PlantPredictionResponse>, t: Throwable) {
                        _isLoading.value = false
                        _errorMessage.value = "Network error: ${t.message}"
                        Log.e("MyPlantViewModel", "Failure: ${t.message}")
                    }
                })
        }
    }
    fun predictNutrient(
        token: String,
        imageUri: Uri?,
        context: Context,
        onSuccess: (NutrientPredictionResponse) -> Unit
    ) {
        _isLoading.value = true
        val imagePart: MultipartBody.Part? = imageUri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val byteArray = inputStream?.readBytes()
            val requestFile = byteArray?.toRequestBody("image/jpeg".toMediaTypeOrNull())
            requestFile?.let { file ->
                MultipartBody.Part.createFormData("nutrient_img", "filename.jpg", file)
            }
        }

        apiService.predictNutrient("Bearer $token", imagePart).enqueue(object : Callback<NutrientPredictionResponse> {
            override fun onResponse(call: Call<NutrientPredictionResponse>, response: Response<NutrientPredictionResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _nutrientPrediction.value = it
                        onSuccess(it)
                    }
                } else {
                    _errorMessage.value = "Failed to make nutrient prediction: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<NutrientPredictionResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Network error: ${t.message}"
            }
        })
    }


}