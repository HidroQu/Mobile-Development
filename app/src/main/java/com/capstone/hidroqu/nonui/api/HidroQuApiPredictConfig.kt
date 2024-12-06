package com.capstone.hidroqu.nonui.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HidroQuApiPredictConfig {
    companion object {
        private const val BASE_URL = "https://hidroqu-ml-1031788214835.asia-southeast2.run.app/"

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
        fun getApiService(): HidroQuApiService {
            return retrofit.create(HidroQuApiService::class.java)
        }
    }
}