package com.capstone.hidroqu.nonui.api

import com.capstone.hidroqu.nonui.data.AuthResponse
import com.capstone.hidroqu.nonui.data.BasicResponse
import com.capstone.hidroqu.nonui.data.CommunityDetailWrapper
import com.capstone.hidroqu.nonui.data.CommunityRequest
import com.capstone.hidroqu.nonui.data.CommunityResponseWrapper
import com.capstone.hidroqu.nonui.data.DiagnosticHistoryResponseWrapper
import com.capstone.hidroqu.nonui.data.ForgotPasswordRequest
import com.capstone.hidroqu.nonui.data.LoginRequest
import com.capstone.hidroqu.nonui.data.LoginResponse
import com.capstone.hidroqu.nonui.data.MyPlantDetailResponse
import com.capstone.hidroqu.nonui.data.MyPlantDetailWrapper
import com.capstone.hidroqu.nonui.data.MyPlantResponse
import com.capstone.hidroqu.nonui.data.MyPlantResponseWrapper
import com.capstone.hidroqu.nonui.data.MyPostsResponseWrapper
import com.capstone.hidroqu.nonui.data.PlantResponse
import com.capstone.hidroqu.nonui.data.PlantResponseWrapper
import com.capstone.hidroqu.nonui.data.RegisterRequest
import com.capstone.hidroqu.nonui.data.ResetPasswordRequest
import com.capstone.hidroqu.nonui.data.StorePlantRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface HidroQuApiService {
    @POST("api/auth/register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @POST("api/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/auth/forgot-password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<BasicResponse>

    @POST("api/auth/reset-password")
    fun resetPassword(@Body request: ResetPasswordRequest): Call<BasicResponse>

    // New endpoints for "Tanamanku"
    @GET("api/plants")
    fun getPlants(
        @Header("Authorization") token: String // Menambahkan header Authorization
    ): Call<PlantResponseWrapper>

    @POST("api/plants/store")
    fun storePlant(
        @Header("Authorization") token: String,
        @Body request: StorePlantRequest
    ): Call<BasicResponse>

    @GET("api/plants/my-plants")
    fun getMyPlants(
        @Header("Authorization") token: String, // Menambahkan header Authorization
        @Query("page") page: Int
    ): Call<MyPlantResponseWrapper>

    @GET("api/plants/my-plants/{id}")
    fun getMyPlantDetail(
        @Header("Authorization") token: String,
        @Path("id") plantId: Int
    ): Call<MyPlantDetailWrapper>

    // Endpoint untuk mendapatkan riwayat diagnostik tanaman
    @GET("api/plants/my-plants/{id_plant}/diagnostics/{id_diagnostic}")
    fun getDiagnosticHistory(
        @Header("Authorization") token: String,
        @Path("id_plant") plantId: Int,
        @Path("id_diagnostic") diagnosticId: Int
    ): Call<DiagnosticHistoryResponseWrapper>

    // Mendapatkan daftar komunitas
    @GET("api/communities")
    fun getCommunities(
        @Header("Authorization") token: String,
        @Query("page") page: Int // Adding a page query parameter
    ): Call<CommunityResponseWrapper>

    // Mendapatkan detail komunitas tertentu berdasarkan ID atau slug
    @GET("api/communities/{community}")
    fun getCommunityDetail(
        @Header("Authorization") token: String,
        @Path("community") communityIdOrSlug: Int
    ): Call<CommunityDetailWrapper>

    // Mendapatkan daftar postingan pengguna dalam komunitas
    @GET("api/communities/my-posts")
    fun getMyPosts(
        @Header("Authorization") token: String
    ): Call<MyPostsResponseWrapper>

    //multipart api-store
    @Multipart
    @POST("api/communities/store")
    fun storeCommunityPost(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<BasicResponse>
}