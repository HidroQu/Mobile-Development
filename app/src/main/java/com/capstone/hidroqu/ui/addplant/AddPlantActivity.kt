package com.capstone.hidroqu.ui.addplant

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.component.CardAddPlant
import com.capstone.hidroqu.ui.list.ListMyAddPlant
import com.capstone.hidroqu.ui.list.dummyListAMyPlantTanamanku
import com.capstone.hidroqu.ui.list.dummyListMyPlantTanamanku

@Composable
fun AddPlantActivity(
    selectedPlantAdd: ListMyAddPlant?,
    onPlantSelected: (ListMyAddPlant) -> Unit
) {
    var selectedPlant by remember { mutableStateOf(selectedPlantAdd) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Silahkan pilih tanaman Anda",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            dummyListMyPlantTanamanku.forEach { plant ->
                val isSelected = selectedPlant == plant
                CardAddPlant(
                    ListPlant = plant,
                    isSelected = isSelected,
                    onClick = {
                        selectedPlant = if (isSelected) null else plant
                        selectedPlant?.let { onPlantSelected(it) }
                    }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AddPlantActivityPreview() {
    val dummyPlant = dummyListMyPlantTanamanku.firstOrNull()
    AddPlantActivity(
        selectedPlantAdd = dummyPlant,
        onPlantSelected = {}
    )
}
