package com.capstone.hidroqu.ui.screen.chooseplant

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.capstone.hidroqu.nonui.data.BasicResponse
import com.capstone.hidroqu.nonui.data.MyPlantResponse
import com.capstone.hidroqu.nonui.data.PlantResponse
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.component.CardAddPlant
import com.capstone.hidroqu.ui.component.CardChoosePlant
import com.capstone.hidroqu.ui.screen.addplant.AddPlantActivity
import com.capstone.hidroqu.ui.viewmodel.MyPlantViewModel
import com.capstone.hidroqu.ui.viewmodel.ScanPlantViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ChoosePlantActivity(
    diagnoseId: Int?,
    photoUri: String?,
    viewModel: MyPlantViewModel = viewModel(),
//    scanViewModel: ScanPlantViewModel = viewModel(),
    context: Context = LocalContext.current,
    navHostController: NavHostController
) {
    val imageUri = Uri.parse(photoUri)
    // Menangani state tanaman yang dipilih secara internal
    var selectedPlant by remember { mutableStateOf<MyPlantResponse?>(null) }

    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val myPlant by viewModel.myPlants.collectAsState(emptyList()) // Mengobservasi data tanaman
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        token?.let {
            viewModel.fetchMyPlants(it) // Memuat data saat komponen diluncurkan
        } ?: run {
            // Handle the case when token is not availabl
        }
    }
    LaunchedEffect(myPlant) {
        Log.d("ChoosePlantActivity", "Data tanaman: $myPlant")
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
                            val currentDate = Date()
                            Log.d("Current Date", currentDate.toString())
                            val todayWithTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID")).format(currentDate)
                            Log.d("Formatted Date", todayWithTime)
                            Log.d(
                                "ChoosePlantActivity",
                                "Token: $token, PlantId: ${plant.id}, Diagnose ID: $diagnoseId, Date: $todayWithTime"
                            )
                            viewModel.storeDiagnose(
                                token = token!!,
                                myPlantId = plant.id,
                                diagnoseId = diagnoseId!!,
                                diagnoseDate = todayWithTime,
                                imageUri = imageUri,
                                context = context,
                                onSuccess = {
                                    // Navigate through a sequence of routes
                                    navHostController.navigate(Screen.DetailMyPlant.createRoute(plant.id)) {
                                        // Clear the back stack up to this point
                                        popUpTo(Screen.MyPlant.route)

                                        // After navigating to detail my plant, navigate to history
                                        launchSingleTop = true
                                    }
                                },
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
                    items(myPlant) { plant ->
                        val isSelected = selectedPlant == plant
                        CardChoosePlant(
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
