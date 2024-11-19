package com.capstone.hidroqu.ui.screen.resultscantanam

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
fun ResultScanTanamActivity(
    photoUri: String?,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val imageUri = Uri.parse(photoUri)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tanaman Image + Nama Tanaman + Nama Latin
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AsyncImage(
                model = imageUri,
                contentDescription = "User Photo",
                modifier = Modifier
                    .height(150.dp)
                    .widthIn(min = 130.dp, max = 250.dp)
                    .clip(RoundedCornerShape(25.dp)),
                contentScale = ContentScale.Crop
            )
            // Nama Tanaman + Nama Latin
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
                        text = "Timun",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    Text(
                        text = "Nama Latin: ",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Cucumis sativus",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Text(
                    text = "Deskripsi Singkat",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Mentimun, timun, atau ketimun (Cucumis sativus) merupakan tumbuhan yang menghasilkan buah yang dapat dimakan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Tanaman Sejenis
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Tanaman Sejenis",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "1. Timun Armenia\n" +
                            "2. Mentimun Libanon\n" +
                            "3. Timun Inggris\n" +
                            "4. Timun Persia",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        // Cara Menanam
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Cara Menanam",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(
                text = "1. Buat gundukan tanah berukuran enam sampai delapan inci dan taruh tiga biji mentimun dalam bentuk segitiga.\n" +
                        "2. Beri jarak yang sesuai karena tanaman bisa tumbuh cukup besar.\n" +
                        "3. Mentimun menyukai suhu hangat dan sinar matahari langsung.\n" +
                        "4. Siapkan wadah persemaian dan isi dengan pasir.\n" +
                        "5. Buat alur tanam dengan kedalaman 1 cm dan jarak alur sekitar 5 cm.\n" +
                        "6. Sebar benih timun pada alur tanam dan tutup kembali dengan pasir.\n" +
                        "7. Siram sampai media semai lembap.\n" +
                        "8. Pasang lanjaran 2-3 hari sebelum tanam, paling lambat sampai sebelum sulur keluar.\n" +
                        "9. Buat lanjaran setinggi 20-30 cm kemudian diikat.\n" +
                        "10. Transplanting dilakukan pada sore hari sekitar jam 2-3 sore.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview
@Composable
private fun ResultScanTanamActivityPreview() {
    HidroQuTheme {
        ResultScanTanamActivity("Poto Tanam", navController = rememberNavController())
    }
}
