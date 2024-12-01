package com.capstone.hidroqu.ui.screen.historymyplant

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.nonui.data.DiagnosticHistoryData
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.screen.detailmyplant.formatDateWithMonthName
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.MyPlantViewModel
import com.capstone.hidroqu.utils.ListHealthHistory
import com.capstone.hidroqu.utils.getHealthHistoryByPlantAndHealthId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryMyPlantActivity(
    navHostController: NavHostController,
    plantId: Int,
    healthId: Int,
    viewModel: MyPlantViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val plantDiagnostic by viewModel.plantDiagnostic.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState("")
    val formattedDate = formatDateWithMonthName(plantDiagnostic?.diagnostic_history?.diagnostic_date ?: "00/00/0000")
    
    // Fetch plant details once the composable is launched
    LaunchedEffect(plantId, healthId) {
        token?.let {
            viewModel.fetchMyPlantDetailDiagnostic(it, plantId, healthId)
        } ?: run {
            // Handle the case when token is not available
        }
    }

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(
                title = formattedDate,
                navHostController = navHostController
            )
        },
        content = { paddingValues ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (!errorMessage.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = errorMessage ?: "Unknown Error", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth()
                ) {
                    if (plantDiagnostic != null) {
                        DetailHistoryContent(plantDiagnostic)
                    } else {
                        Text(
                            "History tidak ditemukan",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DetailHistoryContent(
    history: DiagnosticHistoryData?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
        Image(
            painter = painterResource(R.drawable.ic_launcher_background), // Replace with actual plant image
            contentDescription = "Plant Image",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp)),
            contentScale = ContentScale.Crop
        )

        // Diagnosis Details
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
                    text = history?.diagnostic_history?.diagnostic?.disease_name ?: "Disease name",
                    style = MaterialTheme.typography.bodyMedium
                )
                // Menampilkan gambar terkait dalam LazyRow
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(
                        history?.diagnostic_history?.diagnostic?.image_disease ?: listOf()
                    ) { photoUrl ->
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Related Photo",
                            modifier = Modifier
                                .widthIn(min = 150.dp)
                                .height(95.dp)
                                .clip(RoundedCornerShape(25.dp)) // Gambar dengan sudut membulat
                                .border(
                                    2.dp,
                                    MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(25.dp)
                                ),
                            contentScale = ContentScale.Crop // Memotong gambar agar sesuai dengan ukuran dan bentuk
                        )
                    }
                }
            }

            // Symptoms and Cause Details
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Gejala serangan",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = history?.diagnostic_history?.diagnostic?.indication ?: "Indication",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(16.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Penyebab",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = history?.diagnostic_history?.diagnostic?.cause ?: "Cause",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
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
    val navHostController = rememberNavController()
    HidroQuTheme {
        HistoryMyPlantActivity(navHostController, 1, 1)
    }
}
