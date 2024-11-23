package com.capstone.hidroqu.ui.screen.addplant

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.nonui.data.PlantResponse
import com.capstone.hidroqu.nonui.data.SharedPreferencesHelper
import com.capstone.hidroqu.ui.component.CardAddPlant
import com.capstone.hidroqu.ui.viewmodel.MyPlantViewModel


@Composable
fun AddPlantActivity(
    viewModel: MyPlantViewModel = viewModel(),
    context: Context = LocalContext.current,
    navHostController: NavHostController
) {

    // Menangani state tanaman yang dipilih secara internal
    var selectedPlant by remember { mutableStateOf<PlantResponse?>(null) }

    val plants by viewModel.plants.collectAsState(emptyList()) // Mengobservasi data tanaman
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        val token = SharedPreferencesHelper(context).getToken()
        if (token != null) {
            viewModel.fetchPlants(token) // Memuat data saat komponen diluncurkan
        } else {
            // Handle the case when token is not availabl
        }
    }
    Scaffold(
        topBar = {
            SimpleLightTopAppBar(
                title = "Pilih tanaman anda",
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
                            navHostController.navigate(
                                Screen.FormAddPlant.createRoute(plantId = plant.id)
                            )
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
                    .fillMaxSize()
                    .padding(20.dp)
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Silahkan pilih tanaman Anda",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // Menetapkan 2 kolom
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(0.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(plants) { plant ->
                        val isSelected = selectedPlant == plant
                        CardAddPlant(
                            ListPlant = plant,
                            isSelected = isSelected,
                            onClick = {
                                selectedPlant = if (isSelected) null else plant
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddPlantActivityPreview() {
    // Preview dengan data dummy
    val navController = rememberNavController()
    AddPlantActivity(navHostController = navController)
}
