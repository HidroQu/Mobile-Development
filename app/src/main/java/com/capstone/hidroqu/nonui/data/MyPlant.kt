package com.capstone.hidroqu.nonui.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StorePlantRequest(
    val plant_id: Int,
    val planting_date: String,
    val notes: String? = null
) : Parcelable

@Parcelize
data class PlantResponse(
    val id: Int,
    val name: String,
    val latin_name: String,
    val icon_plant: String,
    val description: String,
    val planting_guide: String,
    val fertilizer_type: String,
    val fun_fact: String,
    val created_at: String?,
    val updated_at: String?
) : Parcelable


@Parcelize
data class MyPlantResponse(
    val id: Int,
    val user_id: Int,
    val plant_id: Int,
    val planting_date: String,
    val notes: String?,
    val plant: PlantResponse
) : Parcelable


@Parcelize
data class MyPlantDetailResponse(
    val id: Int,
    val plant: PlantResponse,
    val planting_date: String,
    val notes: String?,
    val history: List<PlantHistory>
) : Parcelable

@Parcelize
data class PlantResponseWrapper(
    val data: List<PlantResponse>
) : Parcelable

@Parcelize
data class MyPlantResponseWrapper(
    val data: MyPlantData
) : Parcelable

@Parcelize
data class MyPlantData(
    val data: List<MyPlantResponse> // Menyimpan daftar tanaman
) : Parcelable


@Parcelize
data class PlantHistory(
    val date: String,
    val activity: String,
    val notes: String?
) : Parcelable
