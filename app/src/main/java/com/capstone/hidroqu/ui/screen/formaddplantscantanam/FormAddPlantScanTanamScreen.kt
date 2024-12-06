package com.capstone.hidroqu.ui.screen.formaddplantscantanam

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.nonui.data.PlantResponse
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.MyPlantViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FormAddPlantScanTanamScreen(
    plantId: Int,
    viewModel: MyPlantViewModel = viewModel(),
    context: Context = LocalContext.current,
    navHostController: NavHostController
) {
    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    var plantingDateForServer by remember { mutableStateOf("") }
    var plantingDateForDisplay by remember { mutableStateOf("Pilih Tanggal Tanam") }
    var notes by remember { mutableStateOf("") }

    val plants by viewModel.plants.collectAsState()
    var message by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState()

    val plant = plants.find { it.id == plantId }

    LaunchedEffect(Unit) {
        token?.let {
            viewModel.fetchPlants(it)
        } ?: run {

        }
    }

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(
                title = "Tambah Tanaman",
                navHostController = navHostController
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Button(
                    onClick = {
                        if (token != null && plantingDateForServer.isNotEmpty()) {
                            Log.d(
                                "FormAddPlantActivity",
                                "Token: $token, PlantId: $plantId, PlantingDate: $plantingDateForServer, Notes: $notes"
                            )
                            viewModel.storePlant(
                                token = token!!,
                                plantId = plantId,
                                plantingDate = plantingDateForServer,
                                notes = notes,
                                onSuccess = { response ->
                                    navHostController.navigate(Screen.MyPlantRoute.route)
                                    {
                                        popUpTo(Screen.ScanTanamRoute.route) { inclusive = true }
                                    }
                                    message = "Tanaman berhasil ditambahkan!"
                                }
                            )
                        } else {
                            message = "Pastikan tanggal tanam telah dipilih."
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    enabled = plant != null && plantingDateForServer != "" ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (plant != null)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(text = "Simpan", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    ) { paddingValues ->
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
        } else if (plant != null) {
            FormAddPlantContent(
                plantAdd = plant,
                plantingDate = plantingDateForDisplay,
                onDateSelected = { serverDate, displayDate ->
                    plantingDateForServer = serverDate
                    plantingDateForDisplay = displayDate
                },
                modifier = Modifier.padding(paddingValues),
                note = notes,
                onNoteChanged = { newNote -> notes = newNote }
            )
        } else {
            Text(
                text = "Tanaman tidak ditemukan.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAddPlantContent(
    plantAdd: PlantResponse,
    plantingDate: String?,
    onDateSelected: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    note: String,
    onNoteChanged: (String) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()
            Image(
                painter = rememberAsyncImagePainter(
                    model = plantAdd.icon_plant,
                    imageLoader = imageLoader
                ),
                contentDescription = "Gambar Tanaman",
                modifier = Modifier
                    .height(150.dp)
                    .widthIn(min = 130.dp, max = 200.dp)
                    .clip(RoundedCornerShape(25.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    Text(
                        text = "Nama Tanaman:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = plantAdd.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column {
                    Text(
                        text = "Tanggal Menanam:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = plantingDate ?: "Belum Ditentukan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DatePickerField(
                plantingDate = plantingDate,
                onDateSelected = onDateSelected
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Tambahkan Catatan",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextField(
                value = note,
                onValueChange = { onNoteChanged(it) },
                placeholder = { Text(text = "Isi catatan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onPrimary),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerField(
    plantingDate: String?,
    onDateSelected: (String, String) -> Unit
) {
    val dateDialogState = rememberMaterialDialogState()

    val displayDateText = plantingDate ?: "Pilih Tanggal Tanam"


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { dateDialogState.show() }
            .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.medium
            )
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = "Ikon Kalender",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Tanggal Menanam",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = displayDateText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Pilih")
            negativeButton(text = "Batal")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pilih Tanggal Menanam"
        ) { selectedDate ->
            val formattedDateForServer = selectedDate.atStartOfDay()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val formattedDateForDisplay = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
            onDateSelected(formattedDateForServer, formattedDateForDisplay)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFormAddPlantActivity() {
    val navHostController = rememberNavController()
    HidroQuTheme {
        FormAddPlantScanTanamScreen(1, navHostController = navHostController)
    }
}