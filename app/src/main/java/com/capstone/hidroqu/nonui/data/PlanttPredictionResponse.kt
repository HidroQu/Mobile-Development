//package com.capstone.hidroqu.nonui.data
//
//import com.google.gson.annotations.SerializedName
//
//data class PlanttPredictionResponse(
//
//	@field:SerializedName("data")
//	val data: Data,
//
//	@field:SerializedName("status")
//	val status: String
//)
//
//data class Plant(
//
//	@field:SerializedName("latin_name")
//	val latinName: String,
//
//	@field:SerializedName("name")
//	val name: String,
//
//	@field:SerializedName("description")
//	val description: String,
//
//	@field:SerializedName("planting_guide")
//	val plantingGuide: String,
//
//	@field:SerializedName("duration_plant")
//	val durationPlant: Int,
//
//	@field:SerializedName("fertilizer_type")
//	val fertilizerType: String,
//
//	@field:SerializedName("icon_plant")
//	val iconPlant: String,
//
//	@field:SerializedName("id")
//	val id: Int,
//
//	@field:SerializedName("fun_fact")
//	val funFact: String
//)
//
//data class Data(
//
//	@field:SerializedName("confidence")
//	val confidence: Any,
//
//	@field:SerializedName("plant")
//	val plant: Plant,
//
//	@field:SerializedName("predicted_label")
//	val predictedLabel: String
//)
