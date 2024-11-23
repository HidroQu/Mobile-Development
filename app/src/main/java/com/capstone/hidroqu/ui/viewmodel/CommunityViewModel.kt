package com.capstone.hidroqu.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityViewModel : ViewModel() {

    private val apiService: HidroQuApiService = HidroQuApiConfig.retrofit.create(HidroQuApiService::class.java)

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

    // Fetch all community posts
    fun fetchCommunityPosts(token: String) {
        _isLoading.value = true
        Log.d("CommunityViewModel", "Fetching community posts with token: $token")

        apiService.getCommunities("Bearer $token").enqueue(object : Callback<CommunityResponseWrapper> {
            override fun onResponse(call: Call<CommunityResponseWrapper>, response: Response<CommunityResponseWrapper>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val dataWrapper = response.body()?.data
                    if (dataWrapper != null) {
                        _communityPosts.value = dataWrapper.data // Mengakses daftar PostData dari wrapper
                        Log.d("CommunityViewModel", "Posts fetched: ${dataWrapper.data.size} items")
                    } else {
                        _communityPosts.value = emptyList()
                        Log.e("CommunityViewModel", "Data wrapper is null")
                    }
                } else {
                    _errorMessage.value = "Failed to load community posts: ${response.message()}"
                    Log.e("CommunityViewModel", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CommunityResponseWrapper>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
                Log.e("CommunityViewModel", "Error fetching posts: ${t.message}", t)
            }
        })
    }



    // Fetch community detail
    fun fetchCommunityDetail(token: String, communityId: Int) {
        _isLoading.value = true
        Log.d("CommunityViewModel", "Fetching detail komunitas dengan ID=$communityId menggunakan token")

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
                            Log.d("CommunityViewModel", "Detail komunitas berhasil dimuat: $detailData")
                        } else {
                            _errorMessage.value = "Data detail komunitas null"
                            Log.e("CommunityViewModel", "Detail komunitas null")
                        }
                    } else {
                        _errorMessage.value = "Gagal memuat detail komunitas: ${response.message()}"
                        Log.e("CommunityViewModel", "Response error: ${response.code()} - ${response.message()}")
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
            override fun onResponse(call: Call<MyPostsResponseWrapper>, response: Response<MyPostsResponseWrapper>) {
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
    fun addPost(
        token: String,
        message: String,
        onSuccess: (BasicResponse) -> Unit
    ) {
        val request = CommunityRequest(message)

        apiService.storeCommunity("Bearer $token", request).enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                    Log.d("CommunityViewModel", "Post added successfully")
                } else {
                    _errorMessage.value = "Failed to add post: ${response.message()}"
                    Log.e("CommunityViewModel", "Error adding post: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                _errorMessage.value = "Error adding post: ${t.message}"
                Log.e("CommunityViewModel", "Error: ${t.message}", t)
            }
        })
    }

}
