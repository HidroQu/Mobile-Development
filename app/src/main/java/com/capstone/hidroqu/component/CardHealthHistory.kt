package com.capstone.hidroqu.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.ui.detailmyplant.ListHealthHistory
import com.capstone.hidroqu.ui.detailmyplant.dummyListHealthHistory
import com.capstone.hidroqu.ui.home.dummyListArticles

@Composable
fun CardHealthHistory(listHealthHistory: ListHealthHistory, onClick: () -> Unit, modifier: Modifier = Modifier) {
    // Health History Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.medium)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant, // Warna outline
                    shape = MaterialTheme.shapes.medium // Bentuk sesuai Card
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
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
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = listHealthHistory.date,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            IconButton(onClick = { /* Navigate to health details */ }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "See Health Details",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }


    @Preview(showBackground = false)
    @Composable
    private fun CardHealthHistoryPreview() {
        val navController = rememberNavController()
        dummyListHealthHistory.forEach { healthhistory ->
            CardHealthHistory(
                listHealthHistory = healthhistory,
                onClick = {
                    navController.navigate("DetailArticle/${healthhistory.id}") {
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