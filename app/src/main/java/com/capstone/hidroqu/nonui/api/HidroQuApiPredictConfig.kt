package com.capstone.hidroqu.nonui.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HidroQuApiPredictConfig {
    private const val BASE_URL_PREDICT = "http://168.138.164.252:5000/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_PREDICT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
