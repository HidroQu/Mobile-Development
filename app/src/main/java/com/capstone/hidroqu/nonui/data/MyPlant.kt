package com.capstone.hidroqu.nonui.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
data class PlantResponseWrapper(
    val data: List<PlantResponse>
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
data class MyPlantResponseWrapper(
    val data: MyPlantData
) : Parcelable

@Parcelize
data class MyPlantData(
    val data: List<MyPlantResponse> // Menyimpan daftar tanaman
) : Parcelable

@Parcelize
data class MyPlantDetailResponse(
    val id: Int,
    val user_id: Int,
    val plant_id: Int,
    val planting_date: String,
    val notes: String?,
    val created_at: String?,
    val updated_at: String?,
    val plant: PlantResponse?,
    val user: UserResponse?,
    val diagnostic_histories: List<DiagnosticHistory> = emptyList()
) : Parcelable

@Parcelize
data class MyPlantDetailWrapper(
    val status: String,
    val message: String,
    val data: MyPlantDetailResponse
) : Parcelable

@Parcelize
data class StorePlantRequest(
    val plant_id: Int,
    val planting_date: String,
    val notes: String? = null
) : Parcelable

@Parcelize
data class DiagnosticHistory(
    val id: Int,
    val dateHistory: String, // Tanggal riwayat
    val issue: String, // Masalah atau diagnosis yang tercatat
    val symptoms: String, // Gejala yang terdeteksi
    val cause: String, // Penyebab masalah
    val solution: String, // Solusi yang disarankan
    val relatedPhotos: List<String> // Daftar URL foto terkait
) : Parcelable

@Parcelize
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val email_verified_at: String?,
    val profile_image: String?,
    val bio: String?,
    val created_at: String?,
    val updated_at: String?
) : Parcelable
