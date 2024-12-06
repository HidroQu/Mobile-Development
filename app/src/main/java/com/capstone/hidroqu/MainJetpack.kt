package com.capstone.hidroqu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.navigation.BottomBar
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.authGraph
import com.capstone.hidroqu.navigation.communityGraph
import com.capstone.hidroqu.navigation.homeGraph
import com.capstone.hidroqu.navigation.myPlantGraph
import com.capstone.hidroqu.navigation.profileGraph
import com.capstone.hidroqu.ui.screen.splashscreen.SplashScreen
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.ThemeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainJetpack(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    themeViewModel: ThemeViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val systemUiController = rememberSystemUiController()

    val themeMode by themeViewModel.themeMode.collectAsState()

    val isDarkTheme = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    HidroQuTheme(darkTheme = isDarkTheme){
        systemUiController.setSystemBarsColor(
            color = MaterialTheme.colorScheme.primaryContainer,
        )

        systemUiController.setNavigationBarColor(
            color = MaterialTheme.colorScheme.onPrimary,
        )

        Scaffold(
            bottomBar = {
                if (
                    currentRoute == Screen.Home.route ||
                    currentRoute == Screen.MyPlant.route ||
                    currentRoute == Screen.Community.route ||
                    currentRoute == Screen.Profile.route
                ) {
                    BottomBar(navHostController = navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(navHostController = navController)
                }
                authGraph(navController)
                homeGraph(navController)
                myPlantGraph(navController)
                communityGraph(navController)
                profileGraph(
                    navController = navController,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}
