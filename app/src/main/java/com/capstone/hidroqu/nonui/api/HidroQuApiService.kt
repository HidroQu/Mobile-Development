package com.capstone.hidroqu.nonui.api

import com.capstone.hidroqu.nonui.data.AuthResponse
import com.capstone.hidroqu.nonui.data.BasicResponse
import com.capstone.hidroqu.nonui.data.ForgotPasswordRequest
import com.capstone.hidroqu.nonui.data.LoginRequest
import com.capstone.hidroqu.nonui.data.LoginResponse
import com.capstone.hidroqu.nonui.data.MyPlantDetailResponse
import com.capstone.hidroqu.nonui.data.MyPlantResponse
import com.capstone.hidroqu.nonui.data.MyPlantResponseWrapper
import com.capstone.hidroqu.nonui.data.PlantResponse
import com.capstone.hidroqu.nonui.data.PlantResponseWrapper
import com.capstone.hidroqu.nonui.data.RegisterRequest
import com.capstone.hidroqu.nonui.data.ResetPasswordRequest
import com.capstone.hidroqu.nonui.data.StorePlantRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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
        @Header("Authorization") token: String // Menambahkan header Authorization
    ): Call<MyPlantResponseWrapper>

    @GET("api/plants/my-plants/{id}")
    fun getMyPlantDetail(@Path("id") id: Int): Call<MyPlantDetailResponse>
}