package com.capstone.hidroqu.ui.screen.addplant

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.capstone.hidroqu.ui.component.CardAddPlant
import com.capstone.hidroqu.ui.viewmodel.AddPlantViewModel
import com.capstone.hidroqu.ui.viewmodel.ViewModelFactory


@Composable
fun AddPlantActivity(
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    val viewModel: AddPlantViewModel = viewModel(factory = ViewModelFactory(context))

    // Menangani state tanaman yang dipilih secara internal
    var selectedPlant by remember { mutableStateOf<PlantResponse?>(null) }

    val plants by viewModel.plants.observeAsState(emptyList()) // Mengobservasi data tanaman
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPlants() // Memuat data saat komponen diluncurkan
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
                            navHostController.navigate(Screen.FormAddPlant.createRoute(plantId = plant))
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
                plants.forEach { plant ->
                    val isSelected = selectedPlant == plant
                    CardAddPlant(
                        ListPlant = plant,
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
fun AddPlantActivityPreview() {
    // Preview dengan data dummy
    val navController = rememberNavController()
    AddPlantActivity(navHostController = navController)
}
