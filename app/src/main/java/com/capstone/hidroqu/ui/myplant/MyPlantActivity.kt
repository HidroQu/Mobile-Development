package com.capstone.hidroqu.ui.myplant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun MyPlantActivity(navController: NavHostController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Welcome to My Plant Activity")

        // Button to navigate to DetailMyPlantActivity
        val plant = 1
        Button(
            onClick = {
                navController.navigate("DetailTanamanku/$plant"){
                    popUpTo("Tanamanku") { // Bersihkan halaman Home dari stack
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                } // Navigate with a plantId (1 for example)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Go to Detail My Plant")
        }
    }
}
