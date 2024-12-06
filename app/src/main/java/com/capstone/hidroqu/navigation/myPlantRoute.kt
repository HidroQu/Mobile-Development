package com.capstone.hidroqu.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.addplant.AddPlantScreen
import com.capstone.hidroqu.ui.screen.detailmyplant.DetailMyPlantScreen
import com.capstone.hidroqu.ui.screen.formaddplant.FormAddPlantScreen
import com.capstone.hidroqu.ui.screen.historymyplant.HistoryMyPlantScreen
import com.capstone.hidroqu.ui.screen.myplant.MyPlantScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.myPlantGraph(navController: NavHostController){
    navigation(
        startDestination = Screen.MyPlant.route,
        route = Screen.MyPlantRoute.route
    ){
        //tanamanku
        composable(Screen.MyPlant.route) {
            // Panggil MyPlantActivity di sini
            MyPlantScreen(
                navHostController = navController
            )
        }
        ////add plant
        composable(Screen.AddPlant.route) {
            AddPlantScreen(
                navHostController = navController,
            )
        }
        ////form add plant
        composable(
            route = Screen.FormAddPlant.route,
            arguments = listOf(navArgument("plantId") { type = NavType.IntType })
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getInt("plantId") ?: 0
            FormAddPlantScreen(plantId =  plantId, navHostController = navController)
        }
        /////detailmyplant
        composable(
            route = Screen.DetailMyPlant.route,
            arguments = listOf(navArgument("plantId") { type = NavType.IntType })
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getInt("plantId") ?: 0
            DetailMyPlantScreen(
                plantId = plantId,
                navHostController = navController
            )
        }
        ////history riwayat sakit
        composable(
            route = Screen.HistoryMyPlant.route,
            arguments = listOf(
                navArgument("plantId") { type = NavType.IntType },
                navArgument("healthId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getInt("plantId") ?: 0
            val healthId = backStackEntry.arguments?.getInt("healthId") ?: 0

            HistoryMyPlantScreen(navHostController = navController, plantId = plantId, healthId = healthId)
        }
    }
}