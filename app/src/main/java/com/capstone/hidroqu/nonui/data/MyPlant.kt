package com.capstone.hidroqu.nonui.data

import android.os.Parcelable
import com.google.gson.Gson
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
    val created_at: String,
    val updated_at: String,
    val plant: PlantResponse
) : Parcelable

@Parcelize
data class MyPlantResponseWrapper(
    val status: String,
    val message: String,
    val data: MyPlantData
) : Parcelable

@Parcelize
data class MyPlantData(
    val current_page: Int,
    val data: List<MyPlantResponse>, // Menyimpan daftar tanaman
    val first_page_url: String?,
    val last_page: Int,
    val last_page_url: String?,
    val next_page_url: String?,
    val prev_page_url: String?,
    val total: Int,
    val per_page: Int,
    val from: Int,
    val to: Int,
    val path: String
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
    val diagnostic_histories: List<DiagnosticHistory>
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
data class DiagnosticHistoryResponseWrapper(
    val status: String,
    val message: String,
    val data: DiagnosticHistoryData
) : Parcelable

@Parcelize
data class DiagnosticHistoryData(
    val diagnostic_history: DiagnosticHistory
) : Parcelable

@Parcelize
data class DiagnosticHistory(
    val id: Int,
    val user_plant_id: Int,
    val diagnostic_id: Int,
    val diagnosis_date: String,
    val created_at: String,
    val updated_at: String,
    val diagnostic: DiagnosticResponse
) : Parcelable

@Parcelize
data class DiagnosticResponse(
    val id: Int,
    val disease_name: String,
    val image_disease: String, // Ubah ke List<String>
    val indication: String,
    val cause: String,
    val solution: String,
    val created_at: String,
    val updated_at: String
) : Parcelable {
    fun getParsedImageDisease(): List<String> {
        return try {
            Gson().fromJson(image_disease, Array<String>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

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
