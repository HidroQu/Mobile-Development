package com.capstone.hidroqu.ui.screen.chooseplant

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.ui.component.CardAddPlant
import com.capstone.hidroqu.ui.component.CardChoosePlant
import com.capstone.hidroqu.utils.ListMyPlantTanamanku
import com.capstone.hidroqu.utils.ListPlant
import com.capstone.hidroqu.utils.dummyListPlants

@Composable
fun ChoosePlantActivity(
    navHostController: NavHostController
) {
    // Menangani state tanaman yang dipilih secara internal
    var selectedPlant by remember { mutableStateOf<ListPlant?>(null) }

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(
                title = "Pilih koleksi tanaman anda",
                navHostController = navHostController
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Button(
                    onClick = {
                        selectedPlant?.let { plant ->
                            navHostController.navigate(Screen.Home.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    enabled = selectedPlant != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedPlant != null)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(text = "Lanjut", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(20.dp)
                .padding(paddingValues)
        ) {
            Text(
                text = "Silahkan pilih tanaman Anda",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                dummyListPlants.forEach { plant ->
                    val isSelected = selectedPlant == plant
                    CardChoosePlant(
                        listPlant = plant,
                        isSelected = isSelected,
                        onClick = {
                            selectedPlant = if (isSelected) null else plant
                        }
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ChoosePlantActivityPreview() {
    ChoosePlantActivity(navHostController = rememberNavController())
}
