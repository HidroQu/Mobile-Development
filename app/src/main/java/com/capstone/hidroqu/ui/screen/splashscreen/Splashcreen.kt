package com.capstone.hidroqu.ui.screen.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel: AuthViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Observasi nilai LiveData
    val isUserLoggedIn by viewModel.isUserLoggedIn().observeAsState(initial = false)

    LaunchedEffect(key1 = true) {
        delay(700L)

        if (isUserLoggedIn) {
            navHostController.navigate(Screen.Home.route) {
                popUpTo(0)
            }
        } else {
            navHostController.navigate(Screen.Login.route) {
                popUpTo(0)
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_circle),
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )
    }
}
