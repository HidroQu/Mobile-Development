package com.capstone.hidroqu.ui.screen.formaddplant

import android.os.Build
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.utils.ListMyAddPlant
import com.capstone.hidroqu.utils.getAddPlantById
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FormAddPlantActivity(
    navHostController: NavHostController
) {
    val plantId = navHostController.currentBackStackEntry?.arguments?.getInt("plantId") ?: 0
    val plantAdd = getAddPlantById(plantId)

    var plantingDate by remember { mutableStateOf<String?>(null) }
    var note by remember { mutableStateOf("") } // Catatan pengguna

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
                        plantAdd?.let { plant ->
                            navHostController.navigate(Screen.MyPlant.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    enabled = plantAdd != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (plantAdd != null)
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
        if (plantAdd != null) {
            FormAddPlantContent(
                plantAdd = plantAdd,
                plantingDate = plantingDate,
                onDateSelected = { selectedDate ->
                    plantingDate = selectedDate
                },
                modifier = Modifier.padding(paddingValues),
                note = note,
                onNoteChanged = { newNote -> note = newNote }
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
    plantAdd: ListMyAddPlant,
    plantingDate: String?,
    onDateSelected: (String) -> Unit,
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
            Image(
                painter = painterResource(id = plantAdd.userPlantPhoto),
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
                Text(
                    text = "Nama Tanaman: ${plantAdd.name}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Tanggal Menanam: ${plantingDate ?: "Belum Ditentukan"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DatePickerField(plantingDate, onDateSelected)
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Tambahkan catatan",
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
fun DatePickerField(plantingDate: String?, onDateSelected: (String) -> Unit) {
    val dateDialogState = rememberMaterialDialogState()

    // Tanggal yang akan ditampilkan
    val dateText = plantingDate ?: "Pilih Tanggal Tanam"

    // TextField untuk menampilkan tanggal
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { dateDialogState.show() }
            .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant, shape = MaterialTheme.shapes.medium)
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Tanggal Menanam",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Tanggal yang terpilih atau placeholder
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }

    // MaterialDialog untuk memilih tanggal
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
            val formattedDate = DateTimeFormatter.ofPattern("dd MMM yyyy").format(selectedDate)
            onDateSelected(formattedDate)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFormAddPlantActivity() {
    val navHostController = rememberNavController()
    HidroQuTheme {
        FormAddPlantActivity(navHostController)
    }
}
