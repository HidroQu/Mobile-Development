package com.capstone.hidroqu.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.capstone.hidroqu.nonui.data.MyPlantResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardAlarm(
    listAlarmHome: MyPlantResponse,
    modifier: Modifier = Modifier
) {
    // Parse the planting date
    val plantingDate = LocalDate.parse(
        listAlarmHome.planting_date.split(" ")[0],
        DateTimeFormatter.ISO_DATE
    )

    // Calculate harvest date
    val harvestDate = plantingDate.plusDays(listAlarmHome.plant.duration_plant.toLong())

    // Calculate remaining days
    val remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), harvestDate)

    // Prepare remaining days text
    val remainingDaysText = when {
        remainingDays > 0 -> "Akan dipanen dalam ${remainingDays} hari"
        remainingDays == 0L -> "Hari ini panen"
        else -> "Sudah melewati masa panen"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()

        Image(
            painter = rememberAsyncImagePainter(
                model = listAlarmHome.plant.icon_plant,
                imageLoader = imageLoader
            ),
            contentDescription = "Gambar Tanaman",
            modifier = Modifier.size(40.dp)
        )

        Column {
            Text(
                text = listAlarmHome.plant.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = remainingDaysText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}