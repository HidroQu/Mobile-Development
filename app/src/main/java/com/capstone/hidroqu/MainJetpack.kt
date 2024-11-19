package com.capstone.hidroqu

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import com.capstone.hidroqu.ui.screen.community.CommunityActivity
import com.capstone.hidroqu.ui.screen.detailarticle.DetailArticleScreen
import com.capstone.hidroqu.ui.screen.detailcommunity.DetailPostCommunityActivity
import com.capstone.hidroqu.ui.screen.detailmyplant.DetailMyPlantActivity
import com.capstone.hidroqu.ui.screen.editprofile.EditProfileActivity
import com.capstone.hidroqu.ui.screen.formaddplant.FormAddPlantActivity
import com.capstone.hidroqu.ui.screen.formcommunity.FormAddCommunityActivity
import com.capstone.hidroqu.ui.screen.home.HomeActivity
import com.capstone.hidroqu.ui.screen.login.LoginActivity
import com.capstone.hidroqu.ui.screen.myplant.MyPlantActivity
import com.capstone.hidroqu.ui.screen.profile.ProfileActivity
import com.capstone.hidroqu.ui.screen.register.RegisterActivity
import com.capstone.hidroqu.utils.dummyListUserData

@Composable
fun MainJetpack(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute == Screen.Home.route || currentRoute == Screen.MyPlant.route || currentRoute == Screen.Community.route || currentRoute == Screen.Profile.route) {
                    BottomBar(navHostController = navController)
            }
            else if (currentRoute == Screen.AddPlant.route || currentRoute == Screen.FormAddPlant.route || currentRoute == Screen.Community.route || currentRoute == Screen.Profile.route){
                    BottomBar(navHostController = navController)
            } else{

            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginActivity(navHostController = navController)
            }
            composable(Screen.Register.route) {
                RegisterActivity(navHostController = navController)
            }
            composable(Screen.Home.route) {
                HomeActivity(navHostController = navController)
            }
            composable(Screen.MyPlant.route) {
                // Panggil MyPlantActivity di sini
                MyPlantActivity(
                    navHostController = navController,
                    onAddClicked = {
                        navController.navigate(Screen.AddPlant.route) // Navigasi ke halaman penambahan tanaman
                    },
                    onDetailClicked = { plantId ->
                        navController.navigate(Screen.DetailMyPlant.createRoute(plantId)) // Navigasi ke detail tanaman
                    }
                )
            }
            composable(Screen.AddPlant.route) {
                AddPlantActivity(
                    navHostController = navController,
                )
            }

            composable(
                route = Screen.FormAddPlant.route,
                arguments = listOf(navArgument("plantId") { type = NavType.IntType })
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getInt("plantId") ?: 0
                FormAddPlantActivity(navHostController = navController)
            }


            composable(
                route = Screen.DetailMyPlant.route,
                arguments = listOf(navArgument("plantId") { type = NavType.IntType })
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getInt("plantId") ?: 0
                DetailMyPlantActivity(
                    detailId = plantId,
                    navHostController = navController
                )
            }
            composable(Screen.Community.route) {
                CommunityActivity(
                    navHostController = navController,
                    onAddClicked = {
                        navController.navigate(Screen.AddPostCommunity.route)
                    },
                    onDetailClicked = { postId ->
                        navController.navigate(Screen.DetailCommunity.createRoute(postId))
                    }
                )
            }
            composable(Screen.AddPostCommunity.route) {
                // Tambahkan implementasi halaman untuk Add Post
//                FormAddCommunityActivity(navController = navController)
            }
            composable(
                route = Screen.DetailCommunity.route,
                arguments = listOf(navArgument("postId") { type = NavType.IntType })
            ) { val id = it.arguments?.getString("postId") ?: ""
                val communityId = id.toIntOrNull() ?: 0
                DetailPostCommunityActivity(
                    navHostController = navController,
                    communityId = communityId
                )
            }
            composable(Screen.Profile.route){
                ProfileActivity(navHostController = navController, dummyListUserData.first())
            }
            composable(Screen.EditProfile.route) {
                EditProfileActivity(
                    userData = dummyListUserData.first(), // Mengirim data pengguna
                    onNameChanged = { newName ->
                        // Logika untuk mengupdate nama jika perlu
                    },
                    onBioChanged = { newBio ->
                        // Logika untuk mengupdate bio jika perlu
                    }
                )
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
}
