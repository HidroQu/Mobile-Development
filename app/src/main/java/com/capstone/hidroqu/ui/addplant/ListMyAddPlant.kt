package com.capstone.hidroqu.ui.addplant

import com.capstone.hidroqu.R

data class ListMyAddPlant(
    val plantId: Int,
    val name: String,
    val userPlantPhoto: Int
)

val dummyListAMyPlantTanamanku = listOf(
    ListMyAddPlant(
        plantId = 1,
        name = "Tomat",
        userPlantPhoto = R.drawable.tomat
    ),
    ListMyAddPlant(
        plantId = 2,
        name = "Bayam",
        userPlantPhoto = R.drawable.bayam),
    ListMyAddPlant(
        plantId = 3,
        name = "Selada",
        userPlantPhoto = R.drawable.selada),
    ListMyAddPlant(
        plantId = 4,
        name = "Timun",
        userPlantPhoto = R.drawable.timun),
    ListMyAddPlant(
        plantId = 5,
        name = "Kangkung",
        userPlantPhoto = R.drawable.kangkung)
)

fun getAddPlantById(plantId: Int): ListMyAddPlant? {
    return dummyListAMyPlantTanamanku.find { it.plantId == plantId }
}