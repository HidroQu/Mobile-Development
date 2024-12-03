package com.capstone.hidroqu

import android.net.Uri
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.hidroqu.navigation.BottomBar
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.authGraph
import com.capstone.hidroqu.navigation.communityGraph
import com.capstone.hidroqu.navigation.homeGraph
import com.capstone.hidroqu.navigation.myPlantGraph
import com.capstone.hidroqu.navigation.profileGraph
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
import com.capstone.hidroqu.ui.screen.formaddcomment.FormAddComment
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
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.ThemeViewModel
import com.capstone.hidroqu.utils.dummyListUserData
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
