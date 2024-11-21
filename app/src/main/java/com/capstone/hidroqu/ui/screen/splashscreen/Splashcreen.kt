package com.capstone.hidroqu.ui.screen.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.viewmodel.AuthViewModel

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel: AuthViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userExist = remember { mutableStateOf(false) }

//    // Fetch the user data from the database (Room or any data source)
//    val user = viewModel.getUserFromDatabase() // Replace with your actual data fetching method
//    val email = user?.email ?: ""
//    val password = user?.password ?: ""
//
//    LaunchedEffect(Unit) {
//        if (email.isNotEmpty() && password.isNotEmpty()) {
//            // Use the dynamic email and password for login
//            viewModel.loginUser(
//                email = email,
//                password = password,
//                onSuccess = {
//                    userExist.value = true
//                    delay(300L)
//                    navHostController.navigate("home") {
//                        popUpTo(0)
//                    }
//                },
//                onError = {
//                    userExist.value = false
//                    delay(300L)
//                    navHostController.navigate("login") {
//                        popUpTo(0)
//                    }
//                }
//            )
//        } else {
//            // Handle case where no user data is available
//            navHostController.navigate("login") {
//                popUpTo(0)
//            }
//        }
//    }

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


