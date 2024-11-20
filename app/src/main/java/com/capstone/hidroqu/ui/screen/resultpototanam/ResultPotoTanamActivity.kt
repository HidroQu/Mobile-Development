package com.capstone.hidroqu.ui.screen.resultpototanam

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun ResultPotoTanamActivity(
    photoUri: String?,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Sample data for the diagnosis
    val issue = remember { mutableStateOf("Kekurangan sulfur") }
    val symptoms = remember { mutableStateOf("Daun menguning") }
    val cause = remember { mutableStateOf("Kekurangan sulfur dalam tanah") }
    val relatedPhotos = remember { mutableStateOf(listOf(R.drawable.ic_launcher_background, R.drawable.scan_tanam)) }

    // Display the content with the diagnosis
    ResultPotoTanamActivityContent(
        photoUri = photoUri,
        issue = issue.value,
        symptoms = symptoms.value,
        cause = cause.value,
        relatedPhotos = relatedPhotos.value,
        navHostController = navHostController, // Pass navHostController to content
        modifier = modifier
    )
}

@Composable
fun ResultPotoTanamActivityContent(
    photoUri: String?,
    issue: String,
    symptoms: String,
    cause: String,
    relatedPhotos: List<Int>,
    navHostController: NavHostController,  // Add NavHostController here
    modifier: Modifier = Modifier
) {
    val imageUri = Uri.parse(photoUri)

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(
                title = "Penyakit terdeteksi",
                navHostController = navHostController
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Button(
                    onClick = {
                        navHostController.navigate(Screen.ChoosePlant.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Simpan riwayat penyakit",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = Uri.parse(photoUri),
                contentDescription = "Hasil Foto",
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
                // Diagnosis Result
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Hasil diagnosis",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = issue,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(relatedPhotos) { photo ->
                            Image(
                                painter = painterResource(photo),
                                contentDescription = "Related Photo",
                                modifier = Modifier
                                    .widthIn(min = 150.dp)
                                    .height(95.dp)
                                    .clip(RoundedCornerShape(25.dp))
                                    .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(25.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                // Symptoms Section
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Gejala serangan",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = symptoms,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp)
                    )
                }

                // Cause Section
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Penyebab",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = cause,
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
}

@Preview(showBackground = true)
@Composable
private fun ResultPotoTanamActivityPreview() {
    HidroQuTheme {
        ResultPotoTanamActivity("Poto Tanam", navHostController = rememberNavController())
    }
}
