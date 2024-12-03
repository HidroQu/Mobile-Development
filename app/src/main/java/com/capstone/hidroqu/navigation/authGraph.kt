package com.capstone.hidroqu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.forgetpassword.ForgotPasswordActivity
import com.capstone.hidroqu.ui.screen.login.LoginActivity
import com.capstone.hidroqu.ui.screen.register.RegisterActivity
import com.capstone.hidroqu.ui.screen.resetpassword.ResetPasswordActivity

fun NavGraphBuilder.authGraph(navController: NavHostController){
    navigation(
        startDestination = Screen.Login.route,
        route = Screen.AuthRoute.route
    ){
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
    }
}