package com.capstone.hidroqu.nonui.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HidroQuApiConfig {
    private const val BASE_URL = "http://168.138.164.252/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}