package com.capstone.hidroqu.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.BasicResponse
import com.capstone.hidroqu.nonui.data.PlantResponseWrapper
import com.capstone.hidroqu.nonui.data.ProfileResponse
import com.capstone.hidroqu.nonui.data.User
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
    fun uriToBase64(uri: Uri, context: Context): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes()
            inputStream?.close()

            byteArray?.let {
                if (it.size <= 2 * 1024 * 1024) { // Pastikan ukurannya <= 2MB
                    Base64.encodeToString(it, Base64.NO_WRAP) // Konversi ke base64
                } else {
                    Log.e("ProfileViewModel", "File size exceeds 2MB.")
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun updateProfile(
        token: String,
        name: String,
        email: String,
        bio: String,
        context: Context,
        photoUri: Uri?,
        password: String?,
        onComplete: (Boolean, String) -> Unit
    ) {
        _isLoading.value = true
//        // Konversi data menjadi RequestBody
//        val nameRequest = name.toRequestBody("text/plain".toMediaTypeOrNull())
//        val emailRequest = email.toRequestBody("text/plain".toMediaTypeOrNull())
//        val bioRequest = bio.toRequestBody("text/plain".toMediaTypeOrNull())
//
//        val passwordRequest = password?.toRequestBody("text/plain".toMediaTypeOrNull())
//        val passwordConfirmationRequest = password?.toRequestBody("text/plain".toMediaTypeOrNull())
//
//        val photoPart: MultipartBody.Part? = photoUri?.let { uri ->
//            if (isImageValid(uri, context)) {
//                val inputStream = context.contentResolver.openInputStream(uri)
//                val byteArray = inputStream?.readBytes()
//                inputStream?.close()
//
//                byteArray?.let { bytes ->
//                    val requestFile = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
//                    MultipartBody.Part.createFormData("photo", "profile.jpg", requestFile)
//                }
//            } else {
//                Log.e("ProfileViewModel", "Invalid image format or size.")
//                null
//            }
//        }

        Log.d("ProfileViewModel", "Request: name=$name, email=$email, bio=$bio, password=$password, photoUri=$photoUri")
//        Log.d("ProfileUpdate", "NameRequest: $nameRequest, EmailRequest: $emailRequest, BioRequest: $bioRequest")
        val photoBase64: String? = photoUri?.let { uriToBase64(it, context) }

        apiService.updateProfile(
            token = "Bearer $token",
            name = name,
            email = email,
            bio = bio,
            photo = photoBase64,
            password = password,
            passwordConfirmation = password
        ).enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    onComplete(true, "Profil berhasil diperbarui")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ProfileViewModel", "Update failed: $errorBody")
                    onComplete(false, errorBody ?: "Gagal memperbarui profil")
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("ProfileViewModel", "Network error: ${t.message}")
                onComplete(false, t.message ?: "Error tidak diketahui")
            }
        })
    }
}