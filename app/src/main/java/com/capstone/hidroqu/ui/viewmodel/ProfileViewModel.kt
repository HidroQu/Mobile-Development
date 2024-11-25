package com.capstone.hidroqu.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URL

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
    fun isImageValid(uri: Uri, context: Context): Boolean {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes()
            inputStream?.close()

            // Pastikan file tidak null dan ukurannya <= 2MB
            byteArray != null && byteArray.size <= 2 * 1024 * 1024
        } catch (e: Exception) {
            e.printStackTrace()
            false
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
        confirmPassword: String?,
        onComplete: (Boolean, String) -> Unit
    ) {
        _isLoading.value = true
        // Konversi data menjadi RequestBody
        val nameRequest = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailRequest = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val bioRequest = bio.toRequestBody("text/plain".toMediaTypeOrNull())

        val passwordRequest = password?.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordConfirmationRequest = confirmPassword?.toRequestBody("text/plain".toMediaTypeOrNull())
        val method = RequestBody.create("text/plain".toMediaTypeOrNull(), "PUT")

        val photoPart: MultipartBody.Part? = photoUri?.let { uri ->
            try {
                val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg" // Deteksi MIME type
                val byteArray = if (uri.scheme == "http" || uri.scheme == "https") {
                    // Unduh file jika URI adalah URL
                    downloadFile(context, uri)?.readBytes()
                } else {
                    // Baca file lokal sebagai byte array
                    context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                }

                // Validasi ukuran file (<= 2MB)
                if (byteArray != null && byteArray.size > 2 * 1024 * 1024) {
                    Toast.makeText(context, "Ukuran file terlalu besar", Toast.LENGTH_SHORT).show()
                    return@let null
                }

                // Buat RequestBody dan MultipartBody.Part
                val requestFile = byteArray?.toRequestBody(mimeType.toMediaTypeOrNull())
                requestFile?.let {
                    MultipartBody.Part.createFormData(
                        "photo",
                        "filename.${mimeType.split("/").last()}",
                        it
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Gagal membaca file", Toast.LENGTH_SHORT).show()
                null
            }
        }

        Log.d("ProfileViewModel", "Request: name=$name, email=$email, bio=$bio, password=$password, photoUri=$photoUri")
        Log.d("ProfileUpdate", "NameRequest: $nameRequest, EmailRequest: $emailRequest, BioRequest: $bioRequest, PasswordRequest: $passwordRequest, PhotoRequest: $photoPart")

        apiService.updateProfile(
            token = "Bearer $token",
            name = nameRequest,
            email = emailRequest,
            bio = bioRequest,
            password = passwordRequest,
            passwordConfirmation = passwordConfirmationRequest,
            photo = photoPart,
            method = method
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

    // Fungsi untuk mengunduh file jika URI adalah URL
    private fun downloadFile(context: Context, uri: Uri): File? {
        return try {
            val url = URL(uri.toString())
            val connection = url.openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            val tempFile = File(context.cacheDir, "temp_image.jpg")
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}