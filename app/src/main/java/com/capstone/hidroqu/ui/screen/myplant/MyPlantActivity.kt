package com.capstone.hidroqu.ui.screen.myplant

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.component.CardMyPlant
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.TopBarDefault
import com.capstone.hidroqu.ui.viewmodel.MyPlantViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.hidroqu.nonui.data.MyPlantResponse
import com.capstone.hidroqu.nonui.data.SharedPreferencesHelper

@Composable
fun MyPlantActivity(
    navHostController: NavHostController,
    viewModel: MyPlantViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    val myPlants by viewModel.myPlants.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState("")

    LaunchedEffect(Unit) {
        val token = SharedPreferencesHelper(context).getToken()
        if (token != null) {
            viewModel.fetchMyPlants(token)
        } else {
            // Tangani jika token tidak ada (misalnya, arahkan ke halaman login)
        }
    }
    Scaffold(
        topBar = {
            TopBarDefault("Tanamanku")
        },
        floatingActionButton = {
            AddButton(onClick = {
                navHostController.navigate(Screen.AddPlant.route) // Pindah ke halaman AddPlant
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
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
                    Text(text = errorMessage ?: "Unknown Error")
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (myPlants.isEmpty()) {
                        NoPlantList(modifier = Modifier.padding(paddingValues))
                    } else {
                        MyPlantList(
                            plants = myPlants,
                            onDetailClicked = { plantId ->
                                navHostController.navigate(Screen.DetailMyPlant.createRoute(plantId)) {
                                    popUpTo(Screen.MyPlant.route)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
// Loading indicator
@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick, // Navigasi dipicu dari parameter onClick
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = CircleShape,
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Add Plant")
    }
}


@Composable
fun NoPlantList(
    modifier: Modifier = Modifier
) {
    val currentColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.water_drop),
            contentDescription = "water drop",
            colorFilter = ColorFilter.tint(currentColor)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.grass),
            contentDescription = "grass",
            colorFilter = ColorFilter.tint(currentColor)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tidak ada tanaman dikebunmu",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tambahkan tanaman pertama anda untuk memulai merawatnya",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MyPlantList(
    plants: List<MyPlantResponse>,
    modifier: Modifier = Modifier,
    onDetailClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        plants.forEach { plant ->
            CardMyPlant(
                myplant = plant,
                onClick = {
                    onDetailClicked(plant.id)
                }
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MyPlantActivityPreview() {
    HidroQuTheme {
        val navHostController = rememberNavController()

        // Menampilkan MyPlantActivity dengan dummy data
        MyPlantActivity(navHostController, context = LocalContext.current)
    }
}

