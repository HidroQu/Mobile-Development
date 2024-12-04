package com.capstone.hidroqu.nonui.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//object HidroQuApiConfig {
//    private const val BASE_URL = "http://168.138.164.252/"
//
//    val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//}
//https://hidroqu-api-1031788214835.asia-southeast2.run.app/
class HidroQuApiConfig {
    companion object {
        private const val BASE_URL = "http://168.138.164.252/"

        // Instance Retrofit dengan lazy initialization
        private val retrofit: Retrofit by lazy {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        // Akses ApiService dari Retrofit
        fun getApiService(): HidroQuApiService {
            return retrofit.create(HidroQuApiService::class.java)
        }
    }
}