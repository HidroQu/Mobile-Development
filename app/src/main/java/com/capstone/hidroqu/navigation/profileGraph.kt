package com.capstone.hidroqu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.editprofile.EditProfileScreen
import com.capstone.hidroqu.ui.screen.profile.ProfileScreen
import com.capstone.hidroqu.ui.viewmodel.ThemeViewModel

fun NavGraphBuilder.profileGraph(
    navController: NavHostController,
    themeViewModel: ThemeViewModel
) {
    navigation(
        startDestination = Screen.Profile.route,
        route = Screen.ProfileRoute.route
    ) {
        composable(Screen.Profile.route) {
            ProfileScreen(
                navHostController = navController,
                themeViewModel = themeViewModel
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                navHostController = navController
            )
        }
    }
}