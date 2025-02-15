package com.capstone.hidroqu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.camera.CameraPermissionScreen
import com.capstone.hidroqu.ui.screen.chooseplant.ChoosePlantScreen
import com.capstone.hidroqu.ui.screen.resultpototanam.ResultPotoTanamScreen

fun NavGraphBuilder.potoTanamGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.CameraPotoTanam.route,
        route = Screen.PotoTanamRoute.route
    ) {
        composable(Screen.CameraPotoTanam.route) {
            CameraPermissionScreen("Poto Tanam", navController)
        }
        /////detail kamera
        composable(
            route = Screen.ResultPotoTanam.route,
            arguments = listOf(navArgument("photoUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val photoUri = backStackEntry.arguments?.getString("photoUri")
            ResultPotoTanamScreen(photoUri = photoUri, navHostController = navController)
        }
        composable(
            route = Screen.ChoosePlant.route,
            arguments = listOf(
                navArgument("diagnoseId") { type = NavType.IntType },
                navArgument("photoUri") { type = NavType.StringType }
            )

        ) { backStackEntry ->
            val diagnoseId = backStackEntry.arguments?.getInt("diagnoseId")
            val photoUri = backStackEntry.arguments?.getString("photoUri")
            ChoosePlantScreen(
                diagnoseId = diagnoseId,
                photoUri = photoUri,
                navHostController = navController
            )
        }
    }
}