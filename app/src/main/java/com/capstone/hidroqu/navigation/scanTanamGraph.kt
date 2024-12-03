package com.capstone.hidroqu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.article.ArticleActivity
import com.capstone.hidroqu.ui.screen.camera.CameraPermissionScreen
import com.capstone.hidroqu.ui.screen.chooseplant.ChoosePlantActivity
import com.capstone.hidroqu.ui.screen.detailarticle.DetailArticleScreen
import com.capstone.hidroqu.ui.screen.home.HomeActivity
import com.capstone.hidroqu.ui.screen.resultpototanam.ResultPotoTanamActivity
import com.capstone.hidroqu.ui.screen.resultscantanam.ResultScanTanamActivity

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
            ResultScanTanamActivity(photoUri = photoUri, navHostController = navController)
        }
    }
}