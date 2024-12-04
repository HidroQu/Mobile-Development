package com.capstone.hidroqu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.community.CommunityActivity
import com.capstone.hidroqu.ui.screen.detailcommunity.DetailPostCommunityActivity
import com.capstone.hidroqu.ui.screen.forgetpassword.ForgotPasswordActivity
import com.capstone.hidroqu.ui.screen.formaddcomment.FormAddComment
import com.capstone.hidroqu.ui.screen.formcommunity.FormAddCommunityActivity
import com.capstone.hidroqu.ui.screen.login.LoginActivity
import com.capstone.hidroqu.ui.screen.profileother.ProfileOtherActivity
import com.capstone.hidroqu.ui.screen.register.RegisterActivity
import com.capstone.hidroqu.ui.screen.resetpassword.ResetPasswordActivity

fun NavGraphBuilder.communityGraph(navController: NavHostController){
    navigation(
        startDestination = Screen.Community.route,
        route = Screen.CommunityRoute.route
    ){
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

        composable(
            route = Screen.ProfileOther.route,
            arguments = listOf(navArgument("idPost") { type = NavType.IntType })
        ) { backStackEntry ->
            val idPost = backStackEntry.arguments?.getInt("idPost") ?: 0
            ProfileOtherActivity(
                navHostController = navController,
                idPost = idPost
            )
        }


        composable(
            route = Screen.AddPostComment.route,
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            FormAddComment(
                navHostController = navController,
                postId = postId
            )
        }
    }
}