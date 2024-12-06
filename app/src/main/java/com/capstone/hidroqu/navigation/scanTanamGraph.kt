package com.capstone.hidroqu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.camera.CameraPermissionScreen
import com.capstone.hidroqu.ui.screen.formaddplantscantanam.FormAddPlantScanTanamScreen
import com.capstone.hidroqu.ui.screen.resultscantanam.ResultScanTanamScreen

fun NavGraphBuilder.scanTanamGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.CameraScanTanam.route,
        route = Screen.ScanTanamRoute.route
    ) {
        composable(Screen.CameraScanTanam.route) {
            CameraPermissionScreen("Scan Tanam", navController)
        }
        composable(
            route = Screen.ResultScanTanam.route,
            arguments = listOf(navArgument("photoUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val photoUri = backStackEntry.arguments?.getString("photoUri")
            ResultScanTanamScreen(photoUri = photoUri, navHostController = navController)
        }
        composable(
            route = Screen.FormAddPlantScanTanam.route,
            arguments = listOf(navArgument("plantId") { type = NavType.IntType })
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getInt("plantId") ?: 0
            FormAddPlantScanTanamScreen(plantId =  plantId, navHostController = navController)
        }
    }
}