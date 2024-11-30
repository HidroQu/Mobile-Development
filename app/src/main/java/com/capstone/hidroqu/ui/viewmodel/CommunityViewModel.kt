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
                    Log.d("CommunityViewModel", "Response diterima untuk fetchCommunityDetail")
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

    fun storePost(
        token: String,
        title: String,
        content: String,
        imageUri: Uri?,
        context: Context,
        onSuccess: (BasicResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("CommunityViewModel", "Storing post dengan title: $title")
        val titleRequestBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val contentRequestBody = content.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart: MultipartBody.Part? = imageUri?.let {
            // Kompresi gambar sebelum diunggah
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
                        Log.e("CommunityViewModel", "Error menyimpan post: $errorMessage")
                        onError("Error: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    Log.e("CommunityViewModel", "Gagal menyimpan post: ${t.message}", t)
                    onError("Network error: ${t.message}")
                }
            })
        }
    }

    private fun compressImage(context: Context, imageUri: Uri): File? {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)

            // Resize gambar sebelum kompresi
            val resizedBitmap = Bitmap.createScaledBitmap(
                originalBitmap,
                originalBitmap.width / 2,  // Kurangi lebar menjadi setengahnya
                originalBitmap.height / 2, // Kurangi tinggi menjadi setengahnya
                true
            )

            // Calculate compression
            val maxFileSize = 1 * 1024 * 1024 // Kurangi menjadi 1MB
            var quality = 80  // Mulai dari kualitas 80%
            var compressedFile: File

            do {
                val baos = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
                val compressedBytes = baos.toByteArray()

                compressedFile = File(context.cacheDir, "compressed_image.jpg")
                compressedFile.writeBytes(compressedBytes)

                quality -= 10  // Turunkan kualitas lebih cepat
            } while (compressedFile.length() > maxFileSize && quality > 10)

            return compressedFile
        } catch (e: Exception) {
            Log.e("ImageCompression", "Error compressing image", e)
            return null
        }
    }

    fun fetchAllCommunityPosts(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("CommunityViewModel", "Fetching semua postingan komunitas")
            try {
                var currentPage = 1
                val allPosts = mutableListOf<PostData>()
                do {
                    val response = withContext(Dispatchers.IO) {
                        apiService.getCommunities("Bearer $token", currentPage).execute()
                    }
                    if (response.isSuccessful) {
                        response.body()?.data?.let { wrapper ->
                            Log.d(
                                "CommunityViewModel",
                                "Fetched page $currentPage: ${wrapper.data.size} posts"
                            )
                            allPosts.addAll(wrapper.data)
                            currentPage = wrapper.current_page + 1
                        }
                    } else {
                        Log.e(
                            "CommunityViewModel",
                            "Failed to load page $currentPage: ${response.message()}"
                        )
                        _errorMessage.value =
                            "Failed to load page $currentPage: ${response.message()}"
                        break
                    }
                } while (response.body()?.data?.next_page_url != null)

                _communityPosts.value = allPosts
                Log.d(
                    "CommunityViewModel",
                    "Fetched semua postingan komunitas berhasil: ${allPosts.size} posts"
                )
            } catch (e: Exception) {
                Log.e("CommunityViewModel", "Error fetching posts", e)
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
        Log.d("CommunityViewModel", "Storing comment untuk communityId: $communityId")
        Log.d("CommunityViewModel", "Content: $content")
        Log.d("CommunityViewModel", "ImageUri: $imageUri")

        val contentRequestBody = content.toRequestBody("text/plain".toMediaTypeOrNull())

        // Prepare image part
        val imagePart: MultipartBody.Part? = imageUri?.let {
            try {
                Log.d("CommunityViewModel", "Mengakses gambar dari URI: $imageUri")
                val inputStream = context.contentResolver.openInputStream(it)
                val byteArray = inputStream?.readBytes()
                inputStream?.close()
                Log.d("CommunityViewModel", "Ukuran gambar (byte): ${byteArray?.size}")
                val requestFile = byteArray?.toRequestBody("image/jpeg".toMediaTypeOrNull())
                val multipart = requestFile?.let { it1 ->
                    MultipartBody.Part.createFormData("image", "filename.jpg", it1)
                }
                Log.d("CommunityViewModel", "Multipart berhasil dibuat: $multipart")
                multipart
            } catch (e: Exception) {
                Log.e("CommunityViewModel", "Error saat memproses gambar: ${e.message}", e)
                null
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
                Log.d("CommunityViewModel", "Response diterima untuk storeComment")
                Log.d(
                    "CommunityViewModel",
                    "HTTP Code: ${response.code()}, Message: ${response.message()}"
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("CommunityViewModel", "Comment berhasil disimpan: ${it}")
                        Log.d(
                            "CommunityViewModel",
                            "Status: ${it.status}, Message: ${it.message}, Data: ${it.data}"
                        )
                        onSuccess(it)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("CommunityViewModel", "Error menyimpan komentar: $errorBody")
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                Log.e("CommunityViewModel", "Gagal menyimpan komentar: ${t.message}", t)
                onError("Network error: ${t.message}")
            }
        })
    }
}

