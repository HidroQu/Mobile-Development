package com.capstone.hidroqu.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CommunityViewModel : ViewModel() {

    private val apiService: HidroQuApiService =
        HidroQuApiConfig.retrofit.create(HidroQuApiService::class.java)

    private val _communityPosts = MutableStateFlow<List<PostData>>(emptyList())
    val communityPosts: StateFlow<List<PostData>> get() = _communityPosts

    private val _communityDetail = MutableStateFlow<CommunityDetailResponse?>(null)
    val communityDetail: StateFlow<CommunityDetailResponse?> get() = _communityDetail

    private val _myPosts = MutableStateFlow<PostData?>(null)
    val myPosts: StateFlow<PostData?> get() = _myPosts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // Fetch community detail
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
                            Log.d(
                                "CommunityViewModel",
                                "Detail komunitas berhasil dimuat: $detailData"
                            )
                        } else {
                            _errorMessage.value = "Data detail komunitas null"
                            Log.e("CommunityViewModel", "Detail komunitas null")
                        }
                    } else {
                        _errorMessage.value = "Gagal memuat detail komunitas: ${response.message()}"
                        Log.e(
                            "CommunityViewModel",
                            "Response error: ${response.code()} - ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<CommunityDetailWrapper>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Gagal memuat detail komunitas: ${t.message}"
                    Log.e("CommunityViewModel", "Koneksi gagal: ${t.message}", t)
                }
            })
    }


    // Fetch user's posts
    fun fetchMyPosts(token: String) {
        _isLoading.value = true
        apiService.getMyPosts("Bearer $token").enqueue(object : Callback<MyPostsResponseWrapper> {
            override fun onResponse(
                call: Call<MyPostsResponseWrapper>,
                response: Response<MyPostsResponseWrapper>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val postData = response.body()?.data
                    if (postData != null) {
                        _myPosts.value = postData
                        Log.d("CommunityViewModel", "Fetched my post: ID=${postData.id}")
                    } else {
                        Log.e("CommunityViewModel", "My post data is null")
                        _errorMessage.value = "Failed to fetch my post: Data is null"
                    }
                } else {
                    _errorMessage.value = "Failed to load your posts: ${response.message()}"
                    Log.e("CommunityViewModel", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MyPostsResponseWrapper>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
                Log.e("CommunityViewModel", "Error fetching my posts: ${t.message}", t)
            }
        })
    }

    // Add a new post
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
            val inputStream = context.contentResolver.openInputStream(it)
            val byteArray = inputStream?.readBytes()
            val requestFile = byteArray?.toRequestBody("image/jpeg".toMediaTypeOrNull())
            requestFile?.let { it1 ->
                MultipartBody.Part.createFormData("image", "filename.jpg",
                    it1
                )
            }
        }

        viewModelScope.launch {
            apiService.storeCommunityPost(
                "Bearer $token",
                titleRequestBody,
                contentRequestBody,
                imagePart
            ).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onSuccess(it)
                        }
                    } else {
                        onError("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    onError("Network error: ${t.message}")
                }
            })
        }
    }


    // Fungsi untuk mengambil semua postingan komunitas dari semua halaman
    fun fetchAllCommunityPosts(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                var currentPage = 1
                val allPosts = mutableListOf<PostData>()
                do {
                    val response = withContext(Dispatchers.IO) { // Perform network operations on IO thread
                        apiService.getCommunities("Bearer $token", currentPage).execute()
                    }
                    if (response.isSuccessful) {
                        response.body()?.data?.let { wrapper ->
                            allPosts.addAll(wrapper.data)
                            currentPage = wrapper.current_page + 1
                        }
                    } else {
                        _errorMessage.value = "Failed to load page $currentPage: ${response.message()}"
                        break
                    }
                } while (response.body()?.data?.next_page_url != null)

                _communityPosts.value = allPosts
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching posts: ${e.message}"
                Log.e("CommunityViewModel", "Error fetching posts", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
