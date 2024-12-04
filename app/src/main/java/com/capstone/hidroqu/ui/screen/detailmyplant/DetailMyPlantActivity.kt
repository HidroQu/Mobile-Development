package com.capstone.hidroqu.ui.screen.detailmyplant

import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.utils.HarvestNotificationHelper
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.app.Activity
import android.Manifest.permission.POST_NOTIFICATIONS
import android.util.Log
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.hidroqu.utils.formatDate

fun calculateHarvestDate(plantingDate: String, daysToAdd: Int): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("id", "ID"))
            val date = LocalDate.parse(plantingDate.substring(0, 10)) // Hanya ambil bagian tanggal
            val harvestDate = date.plusDays(daysToAdd.toLong()).format(outputFormatter) // Tambah hari dan format
            harvestDate
        } else {
            val inputFormatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
            val outputFormatter = java.text.SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))
            val date = inputFormatter.parse(plantingDate)
            val calendar = java.util.Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }
            calendar.add(java.util.Calendar.DATE, daysToAdd)
            val harvestDate = outputFormatter.format(calendar.time)
            harvestDate
        }
    } catch (e: Exception) {
        "00/00/0000"
    }
}

fun formatDateWithMonthName(dateTime: String): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
            val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
            val date = LocalDate.parse(dateTime.substring(0, 10))
            date.format(outputFormatter)
        } else {
            val inputFormatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
            val outputFormatter = java.text.SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            val date = inputFormatter.parse(dateTime)
            outputFormatter.format(date!!)
        }
    } catch (e: Exception) {
        "00/00/0000"
    }
}

fun formatAndCalculateHarvestDate(plantingDate: String, daysToAdd: Int): Pair<String, String> {
    val formattedDate = formatDate(plantingDate)
    val harvestDate = calculateHarvestDate(plantingDate, daysToAdd)
    return formattedDate to harvestDate
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailMyPlantActivity(
    plantId: Int,
    viewModel: MyPlantViewModel = viewModel(),
    context: Context = LocalContext.current,
    navHostController: NavHostController
) {
    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val plantDetail by viewModel.plantDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState("")
    val context = LocalContext.current
    val notificationHelper = remember { HarvestNotificationHelper(context) }

    LaunchedEffect(plantId) {
        token?.let {
            viewModel.fetchMyPlantDetail(it, plantId)
        } ?: run {
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
fun NoHistoryList(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pupuk),
                contentDescription = "riwayat",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tidak ada riwayat tanaman",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailMyPlantContent(
    plant: MyPlantDetailResponse?,
    healthHistoryList: List<DiagnosticHistory>,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val daysToAdd = plant?.plant?.duration_plant ?: 7
    val (formattedDate, harvestDate) = plant?.planting_date?.let { formatAndCalculateHarvestDate(it, daysToAdd) }
        ?: ("00/00/0000" to "00/00/0000")
    val context = LocalContext.current
    val notificationHelper = remember { HarvestNotificationHelper(context) }
    val userPreferences = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    POST_NOTIFICATIONS
                ) -> {
                    Log.d("NotificationDebug", "Notification permission already granted")
                }
                else -> {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(POST_NOTIFICATIONS),
                        123
                    )
                    Log.d("NotificationDebug", "Requesting notification permission")
                }
            }
        }
    }

    var isNotificationEnabledMap by remember { mutableStateOf(mutableMapOf<Int, Boolean>()) }

    val isNotificationEnabled by plant?.id?.let { plantId ->
        userPreferences.getPlantNotificationEnabled(plantId).collectAsState(initial = false)
    } ?: remember { mutableStateOf(false) }

    var showPermissionDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isNotificationEnabled) {
        plant?.id?.let { plantId ->
            if (isNotificationEnabled) {
                if (!notificationHelper.canScheduleExactAlarms()) {
                    userPreferences.savePlantNotificationEnabled(plantId, false)
                    showPermissionDialog = true
                } else {
                    plant.plant?.name?.let { plantName ->
                        val success = notificationHelper.scheduleHarvestReminders(
                            plantName,
                            calculateHarvestDate(plant.planting_date, daysToAdd)
                        )
                        if (!success) {
                            userPreferences.savePlantNotificationEnabled(plantId, false)
                        }
                    }
                }
            } else {
                notificationHelper.cancelHarvestReminders(
                    calculateHarvestDate(plant.planting_date, daysToAdd)
                )
            }
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Izin Diperlukan") },
            text = { Text("Untuk mengaktifkan notifikasi panen, aplikasi memerlukan izin untuk mengatur alarm. Buka pengaturan untuk memberikan izin.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog = false
                        context.startActivity(notificationHelper.getAlarmPermissionSettingsIntent())
                    }
                ) {
                    Text("Buka Pengaturan")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showPermissionDialog = false }
                ) {
                    Text("Batal")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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

                ),
                contentDescription = "Plant Image",
                modifier = Modifier.size(100.dp)
            )
        }

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
                text = plant?.notes ?: "Tidak ada catatan untuk tanaman ini",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Column(

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Progress tanaman",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(

                        text = "Dalam $daysToAdd hari ke depan, tanamamu siap panen",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.onPrimary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Tanggal menanam",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Column(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.onPrimary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Prediksi panen",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
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
                    onCheckedChange = { newValue ->
                        plant?.id?.let { plantId ->
                            if (newValue) {
                                if (!notificationHelper.canScheduleExactAlarms()) {
                                    showPermissionDialog = true
                                } else {
                                    plant.plant?.name?.let { plantName ->
                                        scope.launch {
                                            val success =
                                                notificationHelper.scheduleHarvestReminders(
                                                    plantName,
                                                    calculateHarvestDate(
                                                        plant.planting_date,
                                                        daysToAdd
                                                    )
                                                )
                                            if (success) {
                                                userPreferences.savePlantNotificationEnabled(
                                                    plantId,
                                                    true
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                scope.launch {
                                    notificationHelper.cancelHarvestReminders(
                                        calculateHarvestDate(plant.planting_date, daysToAdd)
                                    )
                                    userPreferences.savePlantNotificationEnabled(plantId, false)
                                }
                            }
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onSecondary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        if (healthHistoryList.isEmpty()) {
            NoHistoryList(modifier = Modifier.fillMaxWidth())
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
}

@Preview(showBackground = true)
@Composable
fun RiwayatKesehatanPreview() {

    val navHostController = rememberNavController()

    HidroQuTheme {
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
        }
    }
}