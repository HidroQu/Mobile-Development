package com.capstone.hidroqu.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.BasicResponse
import com.capstone.hidroqu.nonui.data.DiagnosticHistoryData
import com.capstone.hidroqu.nonui.data.DiagnosticHistoryResponseWrapper
import com.capstone.hidroqu.nonui.data.LoginResponse
import com.capstone.hidroqu.nonui.data.MyPlantData
import com.capstone.hidroqu.nonui.data.MyPlantDetailResponse
import com.capstone.hidroqu.nonui.data.MyPlantDetailWrapper
import com.capstone.hidroqu.nonui.data.MyPlantResponse
import com.capstone.hidroqu.nonui.data.MyPlantResponseWrapper
import com.capstone.hidroqu.nonui.data.PlantResponse
import com.capstone.hidroqu.nonui.data.PlantResponseWrapper
import com.capstone.hidroqu.nonui.data.StorePlantRequest
import com.capstone.hidroqu.utils.compressImage
import com.capstone.hidroqu.utils.compressImageFile
import com.capstone.hidroqu.utils.isFileSizeValid
import com.capstone.hidroqu.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MyPlantViewModel : ViewModel() {

    private val apiService: HidroQuApiService =
        HidroQuApiConfig.getApiService()

    private val _myPlants = MutableStateFlow<List<MyPlantResponse>>(emptyList())
    val myPlants: StateFlow<List<MyPlantResponse>> get() = _myPlants

    private val _plantDetail = MutableStateFlow<MyPlantDetailResponse?>(null)
    val plantDetail: StateFlow<MyPlantDetailResponse?> get() = _plantDetail

    private val _plantDiagnostic = MutableStateFlow<DiagnosticHistoryData?>(null)
    val plantDiagnostic: StateFlow<DiagnosticHistoryData?> get() = _plantDiagnostic

    private val _plants = MutableStateFlow<List<PlantResponse>>(emptyList())
    val plants: StateFlow<List<PlantResponse>> get() = _plants

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun fetchMyPlants(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                var currentPage = 1
                val allPlants = mutableListOf<MyPlantResponse>() // Ganti tipe ini dengan tipe data yang sesuai
                do {
                    val response = withContext(Dispatchers.IO) {
                        apiService.getMyPlants("Bearer $token", currentPage).execute()
                    }

                    if (response.isSuccessful) {
                        response.body()?.data?.let { wrapper ->
                            allPlants.addAll(wrapper.data)
                            currentPage = wrapper.current_page + 1
                        }
                    } else {
                        _errorMessage.value = "Failed to load page $currentPage: ${response.message()}"
                        break
                    }
                } while (response.body()?.data?.next_page_url != null)

                if (allPlants.isEmpty()) {
                    _errorMessage.value = "You have no plants yet."
                } else {
                    _myPlants.value = allPlants
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching plants: ${e.message}"
                Log.e("MyPlantViewModel", "Error fetching plants", e)
            } finally {
                _isLoading.value = false
            }
        }
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

    fun fetchMyPlantDetailDiagnostic(token: String, plantId: Int, diagnosticId: Int) {
        _isLoading.value = true
        apiService.getDiagnosticHistory(("Bearer $token"), plantId, diagnosticId).enqueue(object : Callback<DiagnosticHistoryResponseWrapper> {
            override fun onResponse(
                call: Call<DiagnosticHistoryResponseWrapper>,
                response: Response<DiagnosticHistoryResponseWrapper>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val plantDetail = response.body()?.data
                    _plantDiagnostic.value = plantDetail
                    Log.d("MyPlantViewModel", "Fetched plant detail: ${response.body()}")
                } else {
                    _errorMessage.value = "Error fetching plant details: ${response.message()}"
                    Log.e("MyPlantViewModel", "Error fetching details: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DiagnosticHistoryResponseWrapper>, t: Throwable) {
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
    fun storeDiagnose(
        token: String,
        diagnoseId: Int,
        myPlantId: Int,
        diagnoseDate: String,
        imageUri: Uri?,
        context: Context,
        onSuccess: (BasicResponse) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Mengubah URI ke file sementara
                var file = withContext(Dispatchers.IO) { uriToFile(imageUri!!, context) }

                // Memastikan ukuran file valid, jika tidak, melakukan kompresi
                if (!isFileSizeValid(file)) {
                    file = withContext(Dispatchers.IO) { compressImageFile(file, context) }
                }

                // Jika ukuran file masih lebih besar dari 1 MB setelah kompresi
                if (!isFileSizeValid(file)) {
                    _isLoading.value = false
                    _errorMessage.value = "File size exceeds 1 MB even after compression."
                    return@launch
                }

                // Mengonversi file ke RequestBody untuk upload
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body =
                    MultipartBody.Part.createFormData("diagnostic_image", file.name, requestFile)

                // Membuat RequestBody untuk parameter lain
                val userPlantIdBody =
                    myPlantId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val diagnoseIdBody =
                    diagnoseId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val diagnoseDateBody = diagnoseDate.toRequestBody("text/plain".toMediaTypeOrNull())

                // Mengirim request ke API untuk menyimpan diagnosis
                apiService.StoreDiagnostic(
                    token = "Bearer $token",
                    userPlant = myPlantId,
                    userPlantId = userPlantIdBody,
                    diagnosticId = diagnoseIdBody,
                    diagnosticDate = diagnoseDateBody,
                    diagnostic_image = body
                ).enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        Log.d("StoreDiagnose", "Response code: ${response.code()}")
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            Log.d("StoreDiagnose", "Response body: $responseBody")

                            responseBody?.let {
                                Log.d("StoreDiagnose", "Diagnose stored successfully")
                                onSuccess(it)
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("StoreDiagnose", "Error storing plant: ${response.message()}")
                            Log.e("StoreDiagnose", "Error body: $errorBody")

                            _errorMessage.value = "Error storing plant: ${response.message()}"
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        Log.e("StoreDiagnose", "Network error: ${t.message}")
                        _errorMessage.value = "Network error: ${t.message}"
                    }
                })

                // Menampilkan status success pada UI
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error storing diagnose: ${e.message}"
                Log.e("StoreDiagnose", "Exception: ${e.message}")
            }
        }

    }
}
