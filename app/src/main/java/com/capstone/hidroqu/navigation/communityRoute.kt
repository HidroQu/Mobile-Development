package com.capstone.hidroqu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.community.CommunityScreen
import com.capstone.hidroqu.ui.screen.detailcommunity.DetailPostCommunityScreen
import com.capstone.hidroqu.ui.screen.formaddcomment.FormAddCommentScreen
import com.capstone.hidroqu.ui.screen.formcommunity.FormAddCommunityScreen
import com.capstone.hidroqu.ui.screen.profileother.ProfileOtherScreen

fun NavGraphBuilder.communityGraph(navController: NavHostController){
    navigation(
        startDestination = Screen.Community.route,
        route = Screen.CommunityRoute.route
    ){
        //komunitas
        composable(Screen.Community.route) {
            CommunityScreen(
                navHostController = navController,
                onAddClicked = {
                    navController.navigate(Screen.AddPostCommunity.route)
                }
            )
        }
        ////add post komunitas
        composable(Screen.AddPostCommunity.route) {
            FormAddCommunityScreen(navHostController = navController)
        }
        ////detail komunitas
        composable(
            route = Screen.DetailCommunity.route,
            arguments = listOf(navArgument("idPost") { type = NavType.IntType })
        ) { backStackEntry ->
            val idPost = backStackEntry.arguments?.getInt("idPost") ?: 0
            DetailPostCommunityScreen(
                navHostController = navController,
                idPost = idPost
            )
        }
        composable(
            route = Screen.ProfileOther.route,
            arguments = listOf(navArgument("idPost") { type = NavType.IntType })
        ) { backStackEntry ->
            val idPost = backStackEntry.arguments?.getInt("idPost") ?: 0
            ProfileOtherScreen(
                navHostController = navController,
                idPost = idPost
            )
        }
        composable(
            route = Screen.AddPostComment.route,
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            FormAddCommentScreen(
                navHostController = navController,
                postId = postId
            )
        }
    }
}