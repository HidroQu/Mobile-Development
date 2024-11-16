package com.capstone.hidroqu.ui.formaddplant

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.capstone.hidroqu.ui.addplant.ListMyAddPlant

@Composable
fun FormAddPlantActivity(plantAdd: ListMyAddPlant) {
    Text(text = "Nama Tanaman: ${plantAdd.name}")
}
