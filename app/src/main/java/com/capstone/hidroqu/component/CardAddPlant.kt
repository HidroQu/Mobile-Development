package com.capstone.hidroqu.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.ui.addplant.ListMyAddPlant
import com.capstone.hidroqu.ui.addplant.dummyListAMyPlantTanamanku
import com.capstone.hidroqu.ui.detailmyplant.ListPlant
import com.capstone.hidroqu.ui.detailmyplant.dummyListPlants

@Composable
fun CardAddPlant(
    ListPlant: ListMyAddPlant,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = ListPlant.userPlantPhoto),
            contentDescription = "Gambar Tanaman",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = ListPlant.name,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview
@Composable
fun CardAddPlantPreview() {
    CardAddPlant(
        ListPlant = dummyListAMyPlantTanamanku[1],
        isSelected = true
    )
}
