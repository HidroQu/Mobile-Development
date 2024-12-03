package com.capstone.hidroqu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.editprofile.EditProfileActivity
import com.capstone.hidroqu.ui.screen.forgetpassword.ForgotPasswordActivity
import com.capstone.hidroqu.ui.screen.login.LoginActivity
import com.capstone.hidroqu.ui.screen.profile.ProfileActivity
import com.capstone.hidroqu.ui.screen.register.RegisterActivity
import com.capstone.hidroqu.ui.screen.resetpassword.ResetPasswordActivity
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
            ProfileActivity(
                navHostController = navController,
                themeViewModel = themeViewModel
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileActivity(
                navHostController = navController
            )
        }
    }
}