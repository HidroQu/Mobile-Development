package com.capstone.hidroqu.ui.addplant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.component.CardAddPlant
import com.capstone.hidroqu.ui.detailmyplant.ListPlant
import com.capstone.hidroqu.ui.detailmyplant.dummyListPlants
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AddPlantActivity(
    selectedPlantAdd: ListMyAddPlant?,
    onPlantSelected: (ListMyAddPlant) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPlant by remember { mutableStateOf<ListMyAddPlant?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Silahkan pilih tanaman Anda",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        dummyListAMyPlantTanamanku.forEach { plant ->
            val isSelected = selectedPlant == plant
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        selectedPlant = plant
                        onPlantSelected(plant)
                    }
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddPlantActivityPreview() {
    MaterialTheme {
        AddPlantActivity(
            selectedPlantAdd = null,
            onPlantSelected = {}
        )
    }
}
