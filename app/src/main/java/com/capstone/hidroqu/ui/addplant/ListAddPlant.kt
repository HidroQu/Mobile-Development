package com.capstone.hidroqu.ui.addplant

import com.capstone.hidroqu.R

data class ListMyAddPlant (
    val imgAddPlant: Int,
    val titleAddPlant: Int
)

val dummyListAMyPlantTanamanku = listOf(
    ListMyAddPlant(R.drawable.tomat, R.string.txt_title_tanamanku1),
    ListMyAddPlant(R.drawable.timun, R.string.txt_title_tanamanku2),
    ListMyAddPlant(R.drawable.selada, R.string.txt_title_tanamanku3),
    ListMyAddPlant(R.drawable.bayam, R.string.txt_title_tanamanku4),
    ListMyAddPlant(R.drawable.kangkung, R.string.txt_title_tanamanku5)
)