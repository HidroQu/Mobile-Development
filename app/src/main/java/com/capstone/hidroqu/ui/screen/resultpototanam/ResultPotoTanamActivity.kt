package com.capstone.hidroqu.ui.screen.resultpototanam

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.hidroqu.ui.screen.myplant.NoPlantList
import com.capstone.hidroqu.ui.viewmodel.ScanPlantViewModel
import java.net.URLEncoder

@Composable
fun ResultPotoTanamActivity(
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
    val nutrientPrediction by viewModel.nutrientPrediction.collectAsState()
    LaunchedEffect(uri) {
        Log.d("ResultPotoTanamActivity", "URI: $uri")
        token?.let {
            Log.d("ResultPotoTanamActivity", "Token found: $it")
            if (uri != null) {
                Log.d("ResultPotoTanamActivity", "Starting nutrient prediction...")
                viewModel.predictNutrient(token!!, uri, context) { response ->
                    Log.d("ResultPotoTanamActivity", "Nutrient prediction success: $response")
                }
            } else {
                Log.e("ResultPotoTanamActivity", "URI is null, navigating to home...")
                navHostController.navigate(Screen.HomeRoute.route)
                {
                    popUpTo(Screen.ResultPotoTanam.route) { inclusive = true }
                }
            }
        } ?: run {
            Log.e("ResultPotoTanamActivity", "Token is null, navigating to home...")
            navHostController.navigate(Screen.HomeRoute.route){
                popUpTo(Screen.ResultPotoTanam.route) { inclusive = true }
            }
        }
    }


    // Handle error cases
    LaunchedEffect(errorMessage) {
        if (errorMessage?.isNotEmpty() == true) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            navHostController.navigate(Screen.HomeRoute.route)
            {
                popUpTo(Screen.ResultPotoTanam.route) { inclusive = true }
            }
        }
    }

    // Display the content with the diagnosis
    ResultPotoTanamActivityContent(
        id = nutrientPrediction?.data?.diagnostic?.id ?: 0,
        photoUri = uri,
        issue = nutrientPrediction?.data?.diagnostic?.name_disease ?: "",
        symptoms = nutrientPrediction?.data?.diagnostic?.indication ?: "",
        cause = nutrientPrediction?.data?.diagnostic?.cause ?: "",
        relatedPhotos = nutrientPrediction?.data?.diagnostic?.disease_image ?: listOf(),
        solution = nutrientPrediction?.data?.diagnostic?.solution ?: "",
        navHostController = navHostController,
        modifier = modifier,
        isLoading = isLoading,
        diagnoseId = nutrientPrediction?.data?.diagnostic?.id ?: 0,
    )
}

@Composable
fun ResultPotoTanamActivityContent(
    id: Int,
    photoUri: Uri?,
    issue: String,
    symptoms: String,
    solution: String,
    cause: String,
    relatedPhotos: List<String>,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    diagnoseId: Int
) {
    Scaffold(
        topBar = {
            SimpleLightTopAppBar(
                title = "Penyakit terdeteksi",
                navHostController = navHostController
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Button(
                    onClick = {
                        val encodedUri = URLEncoder.encode(photoUri.toString(), "UTF-8")
                        navHostController.navigate(
                            Screen.ChoosePlant.createRoute(diagnoseId = diagnoseId, photoUri = encodedUri)
                        )
//                        {
//                            popUpTo(Screen.ResultPotoTanam.route) {inclusive = true}
//                        }
                    },
                    enabled = id != 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Simpan riwayat penyakit",
                        style = MaterialTheme.typography.labelLarge
                    )
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
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = photoUri,
                    contentDescription = "Hasil Foto",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(25.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Diagnosis Result
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "Hasil diagnosis",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = issue,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(relatedPhotos) { photo ->
                                AsyncImage(
                                    model = photo,
                                    contentDescription = "Related Photo",
                                    modifier = Modifier
                                        .widthIn(min = 150.dp)
                                        .height(95.dp)
                                        .clip(RoundedCornerShape(25.dp))
                                        .border(
                                            2.dp,
                                            MaterialTheme.colorScheme.outlineVariant,
                                            RoundedCornerShape(25.dp)
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    // Symptoms Section
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Gejala serangan",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = symptoms,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp)
                        )
                    }

                    // Cause Section
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Penyebab",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = cause,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp)
                        )
                    }

                    // Cause Section
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Solusi",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = solution,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResultPotoTanamActivityPreview() {
    HidroQuTheme {
        ResultPotoTanamActivity("Poto Tanam", navHostController = rememberNavController())
    }
}
