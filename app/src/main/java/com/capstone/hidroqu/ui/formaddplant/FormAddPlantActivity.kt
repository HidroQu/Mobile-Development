package com.capstone.hidroqu.ui.formaddplant

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.list.ListMyAddPlant
import com.capstone.hidroqu.ui.list.getAddPlantById
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FormAddPlantActivity(plantId: Int) {
    val plantAdd = getAddPlantById(plantId)
    var plantingDate by remember { mutableStateOf<String?>(null) } // State untuk tanggal tanam dalam String

    if (plantAdd != null) {
        FormAddPlantContent(plantAdd, plantingDate, onDateSelected = { selectedDate ->
            plantingDate = selectedDate
        })
    } else {
        Text(text = "Tanaman tidak ditemukan.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAddPlantContent(
    plantAdd: ListMyAddPlant,
    plantingDate: String?,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var note by remember { mutableStateOf("") } // State untuk catatan yang diisi oleh user

    Column(
        modifier = Modifier
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
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    Text(
                        text = "Nama Tanaman: ",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = plantAdd.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Tanggal Menanam: ",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = plantingDate ?: "Belum Ditentukan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DatePickerField(plantingDate, onDateSelected)
        } // Menampilkan date picker

        // TextField untuk menambahkan catatan
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Tambahkan catatan",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextField(
                value = note,
                onValueChange = { note = it },
                placeholder = { Text(text = "Isi catatan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onPrimary),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    focusedIndicatorColor = Color.Transparent,  // Remove the bottom focus indicator
                    unfocusedIndicatorColor = Color.Transparent // Remove the bottom unfocused indicator
                )
            )

        }
    }
}

//private val userPlantCollection = mutableListOf<ListMyAddPlant>()
//
//fun addPlantToUserCollection(
//    plant: ListMyAddPlant,
//    plantingDate: String?,
//    note: String
//) {
//    val newPlant = ListMyAddPlant(
//        id = generateUniqueId(),
//        name = plant.name,
//        userPlantPhoto = plant.userPlantPhoto,
//        plantingDate = plantingDate ?: "Tidak Diketahui",
//        note = note
//    )
//    userPlantCollection.add(newPlant)
//}



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
            val formattedDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter.ofPattern("dd MMM yyyy").format(selectedDate)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            onDateSelected(formattedDate)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFormAddPlantActivity() {
    HidroQuTheme {
        FormAddPlantActivity(plantId = 3)
    }
}
