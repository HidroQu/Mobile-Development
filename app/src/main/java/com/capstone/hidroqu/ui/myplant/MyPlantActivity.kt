package com.capstone.hidroqu.ui.myplant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun MyPlantActivity(modifier: Modifier = Modifier) {
    Text(
        text = "Ini Tanamanku!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun MyPlantActivityPreview() {
    HidroQuTheme {
        MyPlantActivity()
    }
}