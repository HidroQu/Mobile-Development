package com.capstone.hidroqu.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.utils.ListHealthHistory
import com.capstone.hidroqu.utils.dummyListHealthHistory
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun CardHealthHistory(listHealthHistory: ListHealthHistory, onClick: () -> Unit, modifier: Modifier = Modifier) {
    // Health History Section
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.medium)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant, // Warna outline
                    shape = MaterialTheme.shapes.medium // Bentuk sesuai Card
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = listHealthHistory.userPlantPhoto),
                    contentDescription = "Gambar Tanaman",
                    modifier = Modifier
                        .size(50.dp) // Ukuran gambar
                        .clip(CircleShape) // Membuat gambar menjadi bulat
                        .border(
                            width = 2.dp, // Ketebalan border
                            color = MaterialTheme.colorScheme.outlineVariant, // Warna outline
                            shape = CircleShape // Bentuk border bulat
                        )
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = listHealthHistory.issue,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = listHealthHistory.dateHistory,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            IconButton(onClick = { onClick() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "See Health Details",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }


    @Preview(showBackground = false)
    @Composable
    private fun CardHealthHistoryPreview() {
        HidroQuTheme {
            val navController = rememberNavController()
            dummyListHealthHistory.forEach { healthhistory ->
                CardHealthHistory(
                    listHealthHistory = healthhistory,
                    onClick = {
                        navController.navigate("DetailArticle/${healthhistory.plantId}") {
                            popUpTo("Home") { // Bersihkan halaman Home dari stack
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }