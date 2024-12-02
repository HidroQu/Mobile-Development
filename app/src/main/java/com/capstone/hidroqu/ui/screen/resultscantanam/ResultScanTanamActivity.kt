package com.capstone.hidroqu.ui.screen.resultscantanam

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.ScanPlantViewModel

@Composable
fun ResultScanTanamActivity(
    photoUri: String?,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ScanPlantViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    val uri = Uri.parse(photoUri)
    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState("")
    val plantPrediction by viewModel.plantPrediction.collectAsState()

    LaunchedEffect(uri) {
        Log.d("ResultPotoTanamActivity", "URI: $uri")
        token?.let {
            Log.d("ResultPotoTanamActivity", "Token found: $it")
            if (uri != null) {
                Log.d("ResultPotoTanamActivity", "Starting nutrient prediction...")
                viewModel.predictPlant(token!!, uri, context) { response ->
                    Log.d("ResultPotoTanamActivity", "Nutrient prediction success: $response")
                }
            } else {
                Log.e("ResultPotoTanamActivity", "URI is null, navigating to home...")
                navHostController.navigate(Screen.Home.route)
            }
        } ?: run {
            Log.e("ResultPotoTanamActivity", "Token is null, navigating to home...")
            navHostController.navigate(Screen.Home.route)
        }
    }

    // Handle error cases
    LaunchedEffect(errorMessage) {
        if (errorMessage?.isNotEmpty() == true) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            navHostController.navigate(Screen.Home.route)  // Navigate to Home if error occurs
        }
    }

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(
                title = "Detail tanaman",
                navHostController = navHostController
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Button(
                    onClick = {
                        val plantId = plantPrediction?.data?.plant?.id
                        plantId?.let {
                            navHostController.navigate(
                                Screen.FormAddPlant.createRoute(plantId = it)
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    enabled = plantPrediction?.data?.plant?.id != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = "Lanjut", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
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
                    model = photoUri,
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
                            text = plantPrediction?.data?.plant?.name ?: "",
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
                            text = plantPrediction?.data?.plant?.latin_name ?: "",
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
                        text = plantPrediction?.data?.plant?.description ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                // Tanaman Sejenis
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Nutrisi Hidroponik",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = plantPrediction?.data?.plant?.fertilizer_type ?: "",
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
                    text = plantPrediction?.data?.plant?.planting_guide ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
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
                    text = "Fun Fact",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(
                    text = buildAnnotatedString {
                        append(plantPrediction?.data?.plant?.fun_fact ?: "Fakta tanaman belum tersedia")
                        append(", ")

                        append(plantPrediction?.data?.plant?.name ?: "Nama tanaman tidak tersedia")

                        append(" biasanya ditanam dalam waktu ")

                        // Menambahkan duration_plant dengan default value jika null
                        append(plantPrediction?.data?.plant?.duration_plant?.toString() ?: "informasi waktu belum tersedia")
                        append(" hari.")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

            }
        }
    }
        }
}

@Preview
@Composable
private fun ResultScanTanamActivityPreview() {
    HidroQuTheme {
        ResultScanTanamActivity("Poto Tanam", navHostController = rememberNavController())
    }
}
