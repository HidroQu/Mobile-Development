package com.capstone.hidroqu.ui.chooseplant

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.detailmyplant.ListPlant
import com.capstone.hidroqu.ui.detailmyplant.dummyListPlants

@Composable
fun ChoosePlantActivity(
    navController: NavController
) {
    val plants = dummyListPlants  // Mengambil data tanaman dari dummyListPlants
    var selectedPlant by remember { mutableStateOf<ListPlant?>(null) }  // State untuk menyimpan tanaman yang dipilih

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Pilih Tanaman untuk menyimpan data diagnosa:", style = MaterialTheme.typography.labelLarge)

        LazyColumn {
            items(plants) { plant ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedPlant = plant  // Menyimpan tanaman yang dipilih
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(plant.userPlantPhoto),
                        contentDescription = "Plant Photo",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(MaterialTheme.shapes.small)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = plant.name)
                }
            }
        }

        // Jika ada tanaman yang dipilih, tampilkan informasinya
        selectedPlant?.let { plant ->
            Text(
                text = "Tanaman yang dipilih: ${plant.name}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        // Tombol untuk menyimpan dan kembali ke Home
        Button(
            onClick = {
                selectedPlant?.let { plant ->
                    // Menyimpan plantId dan navigasi ke Home
                    Toast.makeText(
                        navController.context,
                        "Data disimpan untuk tanaman: ${plant.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("Home")  // Navigasi kembali ke Home setelah menyimpan
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Simpan dan Kembali ke Home")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChoosePlantActivityPreview() {
    ChoosePlantActivity(navController = rememberNavController())
}
