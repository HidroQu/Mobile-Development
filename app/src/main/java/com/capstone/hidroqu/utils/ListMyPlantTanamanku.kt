package com.capstone.hidroqu.utils

import com.capstone.hidroqu.R

data class ListMyPlantTanamanku (
    val imgMyPlantTanamanku: Int,
    val txtMyPlantTanamanku: Int,
    val descMyPlantTanamanku: Int
)

val dummyListAMyPlantTanamanku = listOf(
    ListMyPlantTanamanku(R.drawable.timun, R.string.txt_title_tanamanku1, R.string.txt_desc_tanamanku1),
    ListMyPlantTanamanku(R.drawable.tomat, R.string.txt_title_tanamanku2, R.string.txt_desc_tanamanku2),
    ListMyPlantTanamanku(R.drawable.bayam, R.string.txt_title_tanamanku3, R.string.txt_desc_tanamanku3)
)