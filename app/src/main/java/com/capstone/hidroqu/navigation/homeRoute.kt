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
import com.capstone.hidroqu.ui.screen.forgetpassword.ForgotPasswordActivity
import com.capstone.hidroqu.ui.screen.home.HomeActivity
import com.capstone.hidroqu.ui.screen.login.LoginActivity
import com.capstone.hidroqu.ui.screen.register.RegisterActivity
import com.capstone.hidroqu.ui.screen.resetpassword.ResetPasswordActivity
import com.capstone.hidroqu.ui.screen.resultpototanam.ResultPotoTanamActivity
import com.capstone.hidroqu.ui.screen.resultscantanam.ResultScanTanamActivity

fun NavGraphBuilder.homeGraph(navController: NavHostController){
    navigation(
        startDestination = Screen.Home.route,
        route = Screen.HomeRoute.route
    ){
        //home
        composable(Screen.Home.route) {
            HomeActivity(navHostController = navController)
        }
        potoTanamGraph(navController)
        scanTanamGraph(navController)
        ////camera
//        composable(Screen.CameraScanTanam.route) {
//            CameraPermissionScreen("Scan Tanam", navController)
//        }
//        composable(
//            route = Screen.ResultScanTanam.route,
//            arguments = listOf(navArgument("photoUri") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val photoUri = backStackEntry.arguments?.getString("photoUri")
//            ResultScanTanamActivity(photoUri = photoUri, navHostController = navController)
//        }
        ///artikel
        composable(Screen.Article.route) {
            ArticleActivity(navHostController = navController)
        }
        composable(
            route = Screen.DetailArticle.route,
            arguments = listOf(navArgument("articleId") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("articleId") ?: ""
            val articleId = id.toIntOrNull() ?: 0 // Use `toIntOrNull()` to avoid errors if `id` is empty or invalid.

            DetailArticleScreen(
                navHostController = navController,
                articleId = articleId // Pass the `articleId` here
            )
        }
    }
}