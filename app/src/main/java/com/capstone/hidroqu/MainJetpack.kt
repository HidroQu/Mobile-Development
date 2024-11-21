package com.capstone.hidroqu

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.hidroqu.navigation.BottomBar
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.ui.screen.addplant.AddPlantActivity
import com.capstone.hidroqu.ui.screen.article.ArticleActivity
import com.capstone.hidroqu.ui.screen.camera.CameraPermissionScreen
import com.capstone.hidroqu.ui.screen.chooseplant.ChoosePlantActivity
import com.capstone.hidroqu.ui.screen.community.CommunityActivity
import com.capstone.hidroqu.ui.screen.detailarticle.DetailArticleScreen
import com.capstone.hidroqu.ui.screen.detailcommunity.DetailPostCommunityActivity
import com.capstone.hidroqu.ui.screen.detailmyplant.DetailMyPlantActivity
import com.capstone.hidroqu.ui.screen.editprofile.EditProfileActivity
import com.capstone.hidroqu.ui.screen.forgetpassword.ForgotPasswordActivity
import com.capstone.hidroqu.ui.screen.formaddplant.FormAddPlantActivity
import com.capstone.hidroqu.ui.screen.formcommunity.FormAddCommunityActivity
import com.capstone.hidroqu.ui.screen.historymyplant.HistoryMyPlantActivity
import com.capstone.hidroqu.ui.screen.home.HomeActivity
import com.capstone.hidroqu.ui.screen.login.LoginActivity
import com.capstone.hidroqu.ui.screen.myplant.MyPlantActivity
import com.capstone.hidroqu.ui.screen.profile.ProfileActivity
import com.capstone.hidroqu.ui.screen.register.RegisterActivity
import com.capstone.hidroqu.ui.screen.resetpassword.ResetPasswordActivity
import com.capstone.hidroqu.ui.screen.resultpototanam.ResultPotoTanamActivity
import com.capstone.hidroqu.ui.screen.resultscantanam.ResultScanTanamActivity
import com.capstone.hidroqu.ui.screen.splashscreen.SplashScreen
import com.capstone.hidroqu.utils.dummyListUserData
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainJetpack(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val systemUiController = rememberSystemUiController()

    val context = LocalContext.current
    val intent = (context as? ComponentActivity)?.intent
    val uri: Uri? = intent?.data

    LaunchedEffect(uri) {
        if (uri?.path == "/api/auth/reset-password.*") {
            navController.navigate(Screen.ResetPassword.route)
        }
    }

    systemUiController.setSystemBarsColor(
        color = MaterialTheme.colorScheme.primaryContainer,
    )

    systemUiController.setNavigationBarColor(
        color = MaterialTheme.colorScheme.onPrimary,
    )
    Scaffold(
        bottomBar = {
            if (currentRoute == Screen.Home.route || currentRoute == Screen.MyPlant.route || currentRoute == Screen.Community.route || currentRoute == Screen.Profile.route) {
                    BottomBar(navHostController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            //auth
            composable(Screen.Splash.route) {
                SplashScreen(navHostController = navController)
            }
            composable(Screen.Login.route) {
                LoginActivity(navHostController = navController)
            }
            composable(Screen.Register.route) {
                RegisterActivity(navHostController = navController)
            }
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordActivity(navHostController = navController)
            }
            composable(Screen.ResetPassword.route) {
                ResetPasswordActivity(navHostController = navController)
            }

            //home
            composable(Screen.Home.route) {
                HomeActivity(navHostController = navController)
            }
            ////camera

            composable(Screen.CameraScanTanam.route) {
                CameraPermissionScreen("Scan Tanam", navController)
            }
            composable(Screen.CameraPotoTanam.route) {
                CameraPermissionScreen("Poto Tanam", navController)
            }
            /////detail kamera

            composable(
                route = Screen.ResultPotoTanam.route,
                arguments = listOf(navArgument("photoUri") { type = NavType.StringType })
            ) { backStackEntry ->
                val photoUri = backStackEntry.arguments?.getString("photoUri")
                ResultPotoTanamActivity(photoUri = photoUri, navHostController = navController)
            }



            composable(
                route = Screen.ResultScanTanam.route,
                arguments = listOf(navArgument("photoUri") { type = NavType.StringType })
            ) { backStackEntry ->
                val photoUri = backStackEntry.arguments?.getString("photoUri")
                ResultScanTanamActivity(photoUri = photoUri, navHostController = navController)
            }

            composable(Screen.ChoosePlant.route) {
                ChoosePlantActivity(navHostController = navController)
            }

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
                FormAddPlantActivity(navHostController = navController)
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
            //komunitas
            composable(Screen.Community.route) {
                CommunityActivity(
                    navHostController = navController,
                    onAddClicked = {
                        navController.navigate(Screen.AddPostCommunity.route)
                    }
                )
            }
            ////add post komunitas
            composable(Screen.AddPostCommunity.route) {
                FormAddCommunityActivity(navHostController = navController)
            }
            ////detail komunitas
            composable(
                route = Screen.DetailCommunity.route,
                arguments = listOf(navArgument("idPost") { type = NavType.IntType })
            ) { backStackEntry ->
                val idPost = backStackEntry.arguments?.getInt("idPost") ?: 0
                DetailPostCommunityActivity(
                    navHostController = navController,
                    idPost = idPost
                )
            }

            //profil
            composable(Screen.Profile.route){
                ProfileActivity(navHostController = navController, dummyListUserData.first())
            }
            ////edit profil
            composable(Screen.EditProfile.route) {
                EditProfileActivity(
                    userData = dummyListUserData.first(), // Mengirim data pengguna
                    onNameChanged = { newName ->
                        // Logika untuk mengupdate nama jika perlu
                    },
                    onBioChanged = { newBio ->
                        // Logika untuk mengupdate bio jika perlu
                    },
                    navHostController = navController
                )
            }
        }
    }
}
