package com.capstone.hidroqu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.forgotpassword.ForgotPasswordScreen
import com.capstone.hidroqu.ui.screen.login.LoginScreen
import com.capstone.hidroqu.ui.screen.register.RegisterScreen
import com.capstone.hidroqu.ui.screen.resetpassword.ResetPasswordScreen

fun NavGraphBuilder.authGraph(navController: NavHostController){
    navigation(
        startDestination = Screen.Login.route,
        route = Screen.AuthRoute.route
    ){
        composable(Screen.Login.route) {
            LoginScreen(navHostController = navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navHostController = navController)
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navHostController = navController)
        }
        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(navHostController = navController)
        }
    }
}