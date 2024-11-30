package com.capstone.hidroqu.nonui

import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.MyPlantResponseWrapper

class MyPlantsRepository(private val apiService: HidroQuApiService) {
    suspend fun getMyPlants(token: String): MyPlantResponseWrapper {
        return apiService.getMyPlants("Bearer $token", 1).execute().body()
            ?: throw Exception("Failed to fetch my plants")
    }
}