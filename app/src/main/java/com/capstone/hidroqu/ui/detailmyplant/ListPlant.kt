package com.capstone.hidroqu.ui.detailmyplant

import com.capstone.hidroqu.R

data class ListPlant(
    val id: Int,
    val name: String,
    val latinName: String,
    val note: String,
    val dateSeeding: String,
    val dateHarvest: String,
    val userPlantPhoto: Int
)

val dummyListPlants = listOf(
    ListPlant(
        id = 1,
        name = "Timun",
        latinName = "Cucumis sativus",
        note = "Kemarin nanam pagi, ganti nutrisi 1-2 minggu sekali",
        dateSeeding = "25/08/2024",
        dateHarvest = "25/11/2024",
        userPlantPhoto = R.drawable.timun
    ),
    ListPlant(
        id = 2,
        name = "Tomat",
        latinName = "Tomatus spiritus",
        note = "Kemarin nanam pagi, ganti nutrisi 1-2 minggu sekali",
        dateSeeding = "25/08/2024",
        dateHarvest = "25/11/2024",
        userPlantPhoto = R.drawable.tomat
    ),
    ListPlant(
        id = 3,
        name = "Bayam",
        latinName = "Tomatus spiritus",
        note = "Kemarin nanam pagi, ganti nutrisi 1-2 minggu sekali",
        dateSeeding = "25/08/2024",
        dateHarvest = "25/11/2024",
        userPlantPhoto = R.drawable.bayam
    )
)

fun getPlantById(plantId: Int): ListPlant? {
    return dummyListPlants.find { it.id == plantId }
}

