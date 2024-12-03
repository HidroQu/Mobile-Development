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
import com.capstone.hidroqu.nonui.data.MyPostData
import com.capstone.hidroqu.nonui.data.MyPostsResponseWrapper
import com.capstone.hidroqu.nonui.data.PlantResponseWrapper
import com.capstone.hidroqu.nonui.data.ProfileResponse
import com.capstone.hidroqu.nonui.data.User
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
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileViewModel: ViewModel() {
    private val apiService: HidroQuApiService = HidroQuApiConfig.retrofit.create(HidroQuApiService::class.java)
    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> get() = _userData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _myPost = MutableStateFlow<List<MyPostData>>(emptyList())
    val myPost: StateFlow<List<MyPostData>> get() = _myPost

    fun fetchUserProfile(token: String) {
        _isLoading.value = true
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

    fun getMyPost(
        token: String
    ){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                var currentPage = 1
                val allmypost = mutableListOf<MyPostData>()
                do{
                    val response = withContext(Dispatchers.IO) {
                        apiService.getMyPosts("Bearer $token", currentPage).execute()
                    }

                    if (response.isSuccessful) {
                        response.body()?.data?.let { wrapper ->
                            allmypost.addAll(wrapper.data)
                            currentPage = wrapper.current_page + 1
                        }
                    } else {
                        _errorMessage.value = "Failed to load page $currentPage: ${response.message()}"
                        break
                    }
                } while (response.body()?.data?.next_page_url != null)

                if (allmypost.isEmpty()) {

                } else {
                    val sortedPosts = allmypost.sortedByDescending { post ->
                        try {
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
                                .parse(post.created_at)?.time ?: 0
                        } catch (e: Exception) {
                            0
                        }
                    }
                    _myPost.value = sortedPosts
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching mypost: ${e.message}"
            } finally {
                _isLoading.value = false
            }
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
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Mengubah URI ke file sementara jika ada
                var file: File? = null

                if (photoUri != null) {
                    file = if (photoUri.scheme == "http" || photoUri.scheme == "https") {
                        withContext(Dispatchers.IO) {
                            downloadFile(context, photoUri)
                        }
                    } else {
                        withContext(Dispatchers.IO) {
                            uriToFile(photoUri, context)
                        }
                    }

                    if (file == null) {
                        _isLoading.value = false
                        onComplete(false, "Gagal mengunduh atau membaca file dari URI.")
                        return@launch
                    }

                    // Deteksi MIME type
                    val mimeType = context.contentResolver.getType(photoUri) ?: "image/jpeg"

                    // Memastikan ukuran file valid, jika tidak, melakukan kompresi
                    if (!isFileSizeValid(file)) {
                        file = withContext(Dispatchers.IO) { compressImageFile(file!!, context) }
                    }

                    // Jika ukuran file masih lebih besar dari 2 MB setelah kompresi
                    if (!isFileSizeValid(file)) {
                        _isLoading.value = false
                        onComplete(false, "File size exceeds 2 MB even after compression.")
                        return@launch
                    }

                    // Membuat MultipartBody.Part untuk file
                    val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
                    val photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)

                    // Membuat RequestBody untuk parameter lainnya
                    val nameRequest = name.toRequestBody("text/plain".toMediaTypeOrNull())
                    val emailRequest = email.toRequestBody("text/plain".toMediaTypeOrNull())
                    val bioRequest = bio.toRequestBody("text/plain".toMediaTypeOrNull())
                    val passwordRequest = password?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val passwordConfirmationRequest =
                        confirmPassword?.toRequestBody("text/plain".toMediaTypeOrNull())
                    val method = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())

                    // Mengirim permintaan API
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
                                onComplete(false, errorBody ?: "Gagal memperbarui profil")
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                            _isLoading.value = false
                            onComplete(false, t.message ?: "Error tidak diketahui")
                        }
                    })
                }
            } catch (e: Exception) {
                _isLoading.value = false
                onComplete(false, "Error updating profile: ${e.message}")
            }
        }
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