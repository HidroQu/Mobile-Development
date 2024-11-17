package com.capstone.hidroqu.ui.resultpototanam

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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.theme.HidroQuTheme


@Composable
fun ResultPotoTanamActivity(
    photoUri: String?,
    navController: NavController,
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
    modifier: Modifier = Modifier
) {
    val imageUri = Uri.parse(photoUri)
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
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

            // Display the symptoms section
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

            // Display the cause section
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

@Preview(showBackground = true)
@Composable
private fun ResultPotoTanamActivityPreview() {
    HidroQuTheme {
        ResultPotoTanamActivity("Poto Tanam", navController = rememberNavController())
    }
}
