package com.capstone.hidroqu.ui.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.capstone.hidroqu.nonui.data.PlantResponse
import coil.compose.rememberAsyncImagePainter
import com.capstone.hidroqu.nonui.data.MyPlantResponse

@Composable
fun CardChoosePlant(
    ListPlant: MyPlantResponse,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()

            Image(
                painter = rememberAsyncImagePainter(
                    ListPlant.plant.icon_plant,
                    imageLoader = imageLoader
                ),
                contentDescription = "Gambar Tanaman",
                modifier = Modifier.size(60.dp)
            )
            Log.d("CardAddPlant", "Image URL: ${ListPlant.plant.icon_plant}")

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ListPlant.plant.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

//@Preview
//@Composable
//fun CardChoosePlantPreview() {
//    CardChoosePlant(
//        ListPlant = MyPlantResponse(
//            id = 1,
//            name = "Aloe Vera",
//            latin_name = "Aloe barbadensis miller",
//            icon_plant = "https://4.bp.blogspot.com/-JSteHDHGpV4/UUCsnJmlnWI/AAAAAAAAEps/8A7zoM3T5jU/s1600/Wallpaper+Gambar+Burung+Betet.jpeg", // URL gambar, ganti dengan URL yang valid
//            description = "Aloe vera is a succulent plant species of the genus Aloe.",
//            planting_guide = "Plant in well-drained soil and place it in a sunny spot.",
//            fertilizer_type = "Use a balanced fertilizer every 6-8 weeks.",
//            fun_fact = "Aloe vera is known for its healing properties, especially for skin burns.",
//            created_at = "2024-01-01T10:00:00Z",
//            updated_at = "2024-01-10T12:00:00Z"
//        ),
//        isSelected = false,
//        onClick = {}
//    )
//}