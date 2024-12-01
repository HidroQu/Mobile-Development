package com.capstone.hidroqu.nonui.api

import com.capstone.hidroqu.nonui.data.ArticleDetailWrapper
import com.capstone.hidroqu.nonui.data.ArticleResponseWrapper
import com.capstone.hidroqu.nonui.data.AuthResponse
import com.capstone.hidroqu.nonui.data.BasicResponse
import com.capstone.hidroqu.nonui.data.CommunityDetailWrapper
import com.capstone.hidroqu.nonui.data.CommunityResponseWrapper
import com.capstone.hidroqu.nonui.data.DiagnosticHistoryResponseWrapper
import com.capstone.hidroqu.nonui.data.ForgotPasswordRequest
import com.capstone.hidroqu.nonui.data.LoginRequest
import com.capstone.hidroqu.nonui.data.LoginResponse
import com.capstone.hidroqu.nonui.data.MyPlantDetailWrapper
import com.capstone.hidroqu.nonui.data.MyPlantResponseWrapper
import com.capstone.hidroqu.nonui.data.MyPostResponse
import com.capstone.hidroqu.nonui.data.MyPostsResponseWrapper
import com.capstone.hidroqu.nonui.data.NutrientPredictionResponse
import com.capstone.hidroqu.nonui.data.PlantPredictionResponse
import com.capstone.hidroqu.nonui.data.PlantResponseWrapper
import com.capstone.hidroqu.nonui.data.ProfileResponse
import com.capstone.hidroqu.nonui.data.RegisterRequest
import com.capstone.hidroqu.nonui.data.ResetPasswordRequest
import com.capstone.hidroqu.nonui.data.StorePlantRequest
import com.capstone.hidroqu.nonui.data.TestResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("api/profile")
    fun getUserProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    @Multipart
    @POST("api/profile/update")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("password") password: RequestBody?,
        @Part("password_confirmation") passwordConfirmation: RequestBody?,
        @Part photo: MultipartBody.Part?,
        @Part("_method") method: RequestBody
    ): Call<BasicResponse>


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
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Call<MyPlantResponseWrapper>

    @GET("api/plants/my-plants/{id}")
    fun getMyPlantDetail(
        @Header("Authorization") token: String,
        @Path("id") plantId: Int
    ): Call<MyPlantDetailWrapper>

    @FormUrlEncoded
    @POST("api/plants/my-plants/{user_plant}/diagnostics")
    fun StoreDiagnostic(
        @Header("Authorization") token: String,
        @Path("user_plant") userPlant: Int,
        @Field("user_plant_id") userPlantId: Int,
        @Field("diagnostic_id") diagnosticId: Int,
        @Field("diagnostic_date") diagnosticDate: String,
    ): Call<BasicResponse>

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
        @Query("page") page: Int, // Adding a page query parameter
        @Query("search") searchQuery: String? = null
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
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Call<MyPostResponse>

    //multipart api-store
    @Multipart
    @POST("api/communities/store")
    fun storeCommunityPost(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<BasicResponse>

    @Multipart
    @POST("api/communities/{community}/comment")
    fun storeCommunityComment(
        @Header("Authorization") token: String,
        @Path("community") communityId: Int,
        @Part("comment_id") commentId: Int?,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): Call<TestResponse>

    // Mendapatkan daftar article
    @GET("api/articles")
    fun getArticles(
        @Header("Authorization") token: String,
        @Query("page") page: Int, // Existing parameter
        @Query("search") searchQuery: String? = null // New parameter for search
    ): Call<ArticleResponseWrapper>

    // Mendapatkan daftar komunitas
    @GET("api/articles/{article}")
    fun getArticleDetail(
        @Header("Authorization") token: String,
        @Path("article") articleId: Int
    ): Call<ArticleDetailWrapper>

    // Post Camera
    @Multipart
    @POST("predictNutrient")
    fun predictNutrient(
        @Header("Authorization") token: String,
        @Part nutrient_img: MultipartBody.Part? = null
    ): Call<NutrientPredictionResponse>

    @Multipart
    @POST("predictPlant")
    fun predictPlant(
        @Header("Authorization") token: String,
        @Part plant_img: MultipartBody.Part? = null
    ): Call<PlantPredictionResponse>
}