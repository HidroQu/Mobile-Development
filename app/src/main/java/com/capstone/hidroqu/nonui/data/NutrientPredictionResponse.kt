//package com.capstone.hidroqu.nonui.data
//
//import com.google.gson.annotations.SerializedName
//
//data class NutrientPredictionResponse(
//
//	@field:SerializedName("data")
//	val data: Data,
//
//	@field:SerializedName("status")
//	val status: String
//)
//
//data class Data(
//
//	@field:SerializedName("confidence")
//	val confidence: Any,
//
//	@field:SerializedName("diagnostic")
//	val diagnostic: Diagnostic,
//
//	@field:SerializedName("predicted_label")
//	val predictedLabel: String
//)
//
//data class Diagnostic(
//
//	@field:SerializedName("solution")
//	val solution: String,
//
//	@field:SerializedName("disease_label")
//	val diseaseLabel: String,
//
//	@field:SerializedName("cause")
//	val cause: String,
//
//	@field:SerializedName("name_disease")
//	val nameDisease: String,
//
//	@field:SerializedName("disease_image")
//	val diseaseImage: List<String>,
//
//	@field:SerializedName("id")
//	val id: Int,
//
//	@field:SerializedName("indication")
//	val indication: String
//)
