package com.capstone.hidroqu.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.*
import com.capstone.hidroqu.utils.compressImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class CommunityViewModel : ViewModel() {

    private val apiService: HidroQuApiService =
        HidroQuApiConfig.getApiService()

    private val _communityPosts = MutableStateFlow<List<PostData>>(emptyList())
    val communityPosts: StateFlow<List<PostData>> get() = _communityPosts

    private val _communityDetail = MutableStateFlow<CommunityDetailResponse?>(null)
    val communityDetail: StateFlow<CommunityDetailResponse?> get() = _communityDetail

    private val _myPosts = MutableStateFlow<PostData?>(null)
    val myPosts: StateFlow<PostData?> get() = _myPosts

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> get() = _userData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

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

    fun fetchCommunityDetail(token: String, communityId: Int) {
        _isLoading.value = true
        Log.d(
            "CommunityViewModel",
            "Fetching detail komunitas dengan ID=$communityId menggunakan token"
        )
        apiService.getCommunityDetail("Bearer $token", communityId)
            .enqueue(object : Callback<CommunityDetailWrapper> {
                override fun onResponse(
                    call: Call<CommunityDetailWrapper>,
                    response: Response<CommunityDetailWrapper>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val detailData = response.body()?.data
                        if (detailData != null) {
                            _communityDetail.value = detailData
                        } else {
                            _errorMessage.value = "Data detail komunitas null"
                        }
                    } else {
                        _errorMessage.value = "Gagal memuat detail komunitas: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<CommunityDetailWrapper>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Gagal memuat detail komunitas: ${t.message}"
                }
            })
    }

    fun storePost(
        token: String,
        title: String,
        content: String,
        imageUri: Uri?,
        context: Context,
        onSuccess: (BasicResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val titleRequestBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val contentRequestBody = content.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart: MultipartBody.Part? = imageUri?.let {
            val compressedImageFile = compressImage(context, it)

            compressedImageFile?.let { file ->
                val requestFile = file.readBytes().toRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", "compressed_image.jpg", requestFile)
            }
        }

        viewModelScope.launch {
            apiService.storeCommunityPost(
                "Bearer $token",
                titleRequestBody,
                contentRequestBody,
                imagePart
            ).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onSuccess(it)
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: response.message()
                        onError("Error: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    onError("Network error: ${t.message}")
                }
            })
        }
    }



    fun fetchAllCommunityPosts(token: String, searchQuery: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                var currentPage = 1
                val allPosts = mutableListOf<PostData>()
                do {
                    val response = withContext(Dispatchers.IO) {
                        apiService.getCommunities("Bearer $token", currentPage, searchQuery).execute()
                    }
                    if (response.isSuccessful) {
                        response.body()?.data?.let { wrapper ->
                            allPosts.addAll(wrapper.data)
                            currentPage = wrapper.current_page + 1
                        }
                    } else {
                        _errorMessage.value =
                            "Failed to load page $currentPage: ${response.message()}"
                        break
                    }
                } while (response.body()?.data?.next_page_url != null)

                _communityPosts.value = allPosts
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching posts: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun storeComment(
        token: String,
        communityId: Int,
        content: String,
        imageUri: Uri?,
        context: Context,
        onSuccess: (TestResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val contentRequestBody = content.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart: MultipartBody.Part? = imageUri?.let {
            // Kompresi gambar sebelum diunggah
            val compressedImageFile = compressImage(context, it)

            compressedImageFile?.let { file ->
                val requestFile = file.readBytes().toRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", "filename.jpg", requestFile)
            }
        }

        if (imagePart == null && imageUri != null) {
            Log.e("CommunityViewModel", "Gambar tidak dapat diproses menjadi MultipartBody.Part")
        }

        apiService.storeCommunityComment(
            token = "Bearer $token",
            communityId = communityId,
            commentId = null,
            content = contentRequestBody,
            image = imagePart
        ).enqueue(object : Callback<TestResponse> {
            override fun onResponse(call: Call<TestResponse>, response: Response<TestResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                onError("Network error: ${t.message}")
            }
        })
    }

}

