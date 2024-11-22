package com.capstone.hidroqu.ui.screen.detailmyplant

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.ui.component.CardHealthHistory
import com.capstone.hidroqu.utils.getHealthHistoryByPlantId
import com.capstone.hidroqu.utils.getPlantById
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.MyPlantViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.capstone.hidroqu.nonui.data.DiagnosticHistory
import com.capstone.hidroqu.nonui.data.MyPlantDetailResponse
import com.capstone.hidroqu.nonui.data.PlantResponse
import com.capstone.hidroqu.nonui.data.SharedPreferencesHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatAndCalculateHarvestDate(plantingDate: String, daysToAdd: Int): Pair<String, String> {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Menggunakan java.time untuk API 26 ke atas
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("id", "ID"))
            val date = LocalDate.parse(plantingDate.substring(0, 10)) // Hanya ambil bagian tanggal
            val formattedDate = date.format(outputFormatter) // Format ke dd/MM/yyyy
            val harvestDate = date.plusDays(daysToAdd.toLong()).format(outputFormatter) // Tambah 60 hari dan format
            formattedDate to harvestDate
        } else {
            // Menggunakan SimpleDateFormat untuk API di bawah 26
            val inputFormatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
            val outputFormatter = java.text.SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))
            val date = inputFormatter.parse(plantingDate)
            val formattedDate = outputFormatter.format(date!!)
            val calendar = java.util.Calendar.getInstance()
            calendar.time = date
            calendar.add(java.util.Calendar.DATE, daysToAdd) // Tambah 60 hari
            val harvestDate = outputFormatter.format(calendar.time)
            formattedDate to harvestDate
        }
    } catch (e: Exception) {
        "00/00/0000" to "00/00/0000" // Jika tanggal tidak valid, fallback ke default
    }
}



@Composable
fun DetailMyPlantActivity(
    plantId: Int,
    viewModel: MyPlantViewModel = viewModel(),
    context: Context = LocalContext.current,
    navHostController: NavHostController
) {

    val plantDetail by viewModel.plantDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState("")

    // Fetch plant details once the composable is launched
    LaunchedEffect(plantId) {
        val token = SharedPreferencesHelper(context).getToken()
        if (token != null) {
            viewModel.fetchMyPlantDetail(token, plantId)
        } else {
            // Handle the case when token is not available
        }
    }
        Scaffold(
            topBar = {
                SimpleLightTopAppBar(
                    title = plantDetail?.plant?.name ?: "Plant Details",
                    navHostController = navHostController
                )
            },
            content = { paddingValues ->
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
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
                            .fillMaxSize()
                    ) {
                        DetailMyPlantContent(
                            plant = plantDetail,
                            healthHistoryList = plantDetail?.diagnostic_histories ?: listOf(),
                            navHostController = navHostController,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        )
    }




@Composable
fun DetailMyPlantContent(
    plant: MyPlantDetailResponse?,
    healthHistoryList: List<DiagnosticHistory>,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    var isNotificationEnabled by remember { mutableStateOf(false) }

    val daysToAdd = 60
    val (formattedDate, harvestDate) = plant?.planting_date?.let { formatAndCalculateHarvestDate(it, daysToAdd) }
        ?: ("00/00/0000" to "00/00/0000")

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Image placeholder for the plant
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onPrimary),
            contentAlignment = Alignment.Center
        ) {
            val imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()
            Image(
                painter = rememberAsyncImagePainter(
                        model = plant?.plant?.icon_plant,
                        imageLoader = imageLoader

                ), // Replace with actual plant image
                contentDescription = "Plant Image",
                modifier = Modifier.size(100.dp) // Adjust size as needed
            )
        }

        // Plant Name and Latin Name
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Nama tanaman:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = plant?.plant?.name ?: "Plant name",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Nama Latin:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = plant?.plant?.latin_name ?: "Latin name",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        // Notes Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Catatan",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = plant?.plant?.description ?: "note",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }

        // Plant Progress Section
        Column(

        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Column (
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    Text(
                        text = "Progress tanaman",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(

                        text = "Dalam 60 hari ke depan, tanamamu siap panen",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column (
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.medium)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant, // Warna outline
                                shape = MaterialTheme.shapes.medium // Bentuk sesuai Card
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ){
                        Text(
                            text = "Tanggal menanam",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp)) // Spacing between label and value

                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.medium)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant, // Warna outline
                                shape = MaterialTheme.shapes.medium // Bentuk sesuai Card
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ){
                        Text(
                            text = "Prediksi panen",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp)) // Spacing between label and value
                        Text(
                            text = harvestDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notifikasi panen",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Switch(
                    checked = isNotificationEnabled,
                    onCheckedChange = { isNotificationEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onSecondary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Riwayat kesehatan",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                healthHistoryList.forEach { healthHistory ->
                    CardHealthHistory(
                        listHealthHistory = healthHistory,
                        onClick = {
                            navHostController.navigate(
                                Screen.HistoryMyPlant.createRoute(
                                    plantId = plant?.id ?: 0,
                                    healthId = healthHistory.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DetailMyPlantActivityPreview() {
    HidroQuTheme {
        val navHostController = rememberNavController()
//        DetailMyPlantActivity(2, navHostController)
    }
}