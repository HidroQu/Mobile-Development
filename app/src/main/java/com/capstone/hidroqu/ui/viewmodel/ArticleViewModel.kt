package com.capstone.hidroqu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.ArticleDetailResponse
import com.capstone.hidroqu.nonui.data.ArticleDetailWrapper
import com.capstone.hidroqu.nonui.data.ArticleResponseWrapper
import com.capstone.hidroqu.nonui.data.CommunityDetailResponse
import com.capstone.hidroqu.nonui.data.CommunityDetailWrapper
import com.capstone.hidroqu.nonui.data.PostData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel : ViewModel() {
    private val apiService: HidroQuApiService =
        HidroQuApiConfig.getApiService()
    private val _articles = MutableStateFlow<List<ArticleDetailResponse>>(emptyList())
    val articles: StateFlow<List<ArticleDetailResponse>> get() = _articles

    private val _articlesdetail = MutableStateFlow<ArticleDetailResponse?>(null)
    val articleDetail: StateFlow<ArticleDetailResponse?> get() = _articlesdetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun fetchAllArticles(token: String, searchQuery: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            val allArticles = mutableListOf<ArticleDetailResponse>()

            try {
                var currentPage = 1
                do {
                    val response = withContext(Dispatchers.IO) {
                        apiService.getArticles("Bearer $token", currentPage, searchQuery).execute()
                    }
                    if (response.isSuccessful) {
                        response.body()?.data?.let { wrapper ->
                            val sortedPageArticles = wrapper.data.sortedByDescending { it.id }
                            allArticles.addAll(sortedPageArticles)
                            currentPage = wrapper.currentPage + 1
                        } ?: break
                    } else {
                        _errorMessage.value = response.message()
                        break
                    }
                } while (response.body()?.data?.nextPageUrl != null)
                _articles.value = allArticles.sortedByDescending { it.id }
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching articles: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchArticleDetail(token: String, articleId: Int) {
        _isLoading.value = true
        apiService.getArticleDetail("Bearer $token", articleId)
            .enqueue(object : Callback<ArticleDetailWrapper> {
                override fun onResponse(
                    call: Call<ArticleDetailWrapper>,
                    response: Response<ArticleDetailWrapper>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val detailData = response.body()?.data
                        if (detailData != null) {
                            _articlesdetail.value = detailData
                        } else {
                            _errorMessage.value = "Data detail komunitas null"
                        }
                    } else {
                        _errorMessage.value = "Gagal memuat detail komunitas: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<ArticleDetailWrapper>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Gagal memuat detail komunitas: ${t.message}"
                }
            })
    }
}
