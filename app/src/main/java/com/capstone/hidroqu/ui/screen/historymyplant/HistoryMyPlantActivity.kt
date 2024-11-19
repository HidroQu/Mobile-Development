package com.capstone.hidroqu.ui.screen.historymyplant

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.R
import com.capstone.hidroqu.utils.ListHealthHistory
import com.capstone.hidroqu.utils.getHealthHistoryByPlantAndHealthId
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun HistoryMyPlantActivity(plantId: Int, healthId: Int, modifier: Modifier = Modifier) {
    val history = getHealthHistoryByPlantAndHealthId(plantId, healthId)

    if (history != null) {
        DetailHistoryContent(history)
    } else {
        Text("History tidak ditemukan", style = MaterialTheme.typography.bodyLarge)
    }
}


@Composable
fun DetailHistoryContent(history: ListHealthHistory, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "User Photo",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text (
                    text = "Hasil diagnosis",
                    style = MaterialTheme.typography.labelLarge
                )
                Text (
                    text = history.issue,
                    style = MaterialTheme.typography.bodyMedium
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(history.relatedPhotos) { photo ->
                        Image(
                            painter = painterResource(photo),
                            contentDescription = "Related Photo",
                            modifier = Modifier
                                .widthIn(min = 150.dp)
                                .height(95.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(25.dp)), // Menambahkan border tipis
                            contentScale = ContentScale.Crop
                        )
                    }
                }

            }
            Column (
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = "Gejala serangan",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = history.symptoms,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(16.dp)
                )
            }
            Column (
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = "Penyebab",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = history.cause,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(16.dp)
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun HistoryMyPlantActivityPreview() {
    HidroQuTheme {
        HistoryMyPlantActivity(1, 1)
    }
}