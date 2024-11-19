package com.capstone.hidroqu.ui.screen.myplant

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.utils.dummyListPlants

@Composable
fun MyPlantActivity(
    navHostController: NavHostController,
    onAddClicked: () -> Unit,
    onDetailClicked: (Int) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            AddButton(onClick = {
                navHostController.navigate(Screen.AddPlant.route) // Pindah ke halaman AddPlant
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .fillMaxSize()
        ) {
            if (dummyListPlants.isEmpty()) {
                NoPlantList(modifier = Modifier.padding(paddingValues))
            } else {
                MyPlantList(
                    modifier = Modifier.padding(paddingValues),
                    onDetailClicked = { plantId ->
                        navHostController.navigate(Screen.DetailMyPlant.createRoute(plantId)){
                            popUpTo(Screen.MyPlant.route)
                        }
                    }
                )
            }
        }
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
fun MyPlantList(modifier: Modifier = Modifier, onDetailClicked: (Int) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        dummyListPlants.forEach { plant ->
            CardMyPlant(
                ListPlant = plant,
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
        MyPlantActivity(navHostController,
            onAddClicked = {
            },
            onDetailClicked = {

            }
        )
    }
}
