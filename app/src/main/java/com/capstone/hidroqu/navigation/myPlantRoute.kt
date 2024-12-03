package com.capstone.hidroqu.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.addplant.AddPlantActivity
import com.capstone.hidroqu.ui.screen.detailmyplant.DetailMyPlantActivity
import com.capstone.hidroqu.ui.screen.forgetpassword.ForgotPasswordActivity
import com.capstone.hidroqu.ui.screen.formaddplant.FormAddPlantActivity
import com.capstone.hidroqu.ui.screen.historymyplant.HistoryMyPlantActivity
import com.capstone.hidroqu.ui.screen.login.LoginActivity
import com.capstone.hidroqu.ui.screen.myplant.MyPlantActivity
import com.capstone.hidroqu.ui.screen.register.RegisterActivity
import com.capstone.hidroqu.ui.screen.resetpassword.ResetPasswordActivity

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.myPlantGraph(navController: NavHostController){
    navigation(
        startDestination = Screen.MyPlant.route,
        route = Screen.MyPlantRoute.route
    ){
        //tanamanku
        composable(Screen.MyPlant.route) {
            // Panggil MyPlantActivity di sini
            MyPlantActivity(
                navHostController = navController
            )
        }
        ////add plant
        composable(Screen.AddPlant.route) {
            AddPlantActivity(
                navHostController = navController,
            )
        }
        ////form add plant
        composable(
            route = Screen.FormAddPlant.route,
            arguments = listOf(navArgument("plantId") { type = NavType.IntType })
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getInt("plantId") ?: 0
            FormAddPlantActivity(plantId =  plantId, navHostController = navController)
        }
        /////detailmyplant
        composable(
            route = Screen.DetailMyPlant.route,
            arguments = listOf(navArgument("plantId") { type = NavType.IntType })
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getInt("plantId") ?: 0
            DetailMyPlantActivity(
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
            val plantId = backStackEntry.arguments?.getInt("plantId") ?: 0  // Default to 0 if null
            val healthId = backStackEntry.arguments?.getInt("healthId") ?: 0  // Default to 0 if null

            HistoryMyPlantActivity(navHostController = navController, plantId = plantId, healthId = healthId)
        }
    }
}