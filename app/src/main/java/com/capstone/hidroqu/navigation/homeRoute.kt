package com.capstone.hidroqu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.capstone.hidroqu.ui.screen.article.ArticleScreen
import com.capstone.hidroqu.ui.screen.detailarticle.DetailArticleScreen
import com.capstone.hidroqu.ui.screen.home.HomeScreen

fun NavGraphBuilder.homeGraph(navController: NavHostController){
    navigation(
        startDestination = Screen.Home.route,
        route = Screen.HomeRoute.route
    ){
        //home
        composable(Screen.Home.route) {
            HomeScreen(navHostController = navController)
        }
        potoTanamGraph(navController)
        scanTanamGraph(navController)
        ///artikel
        composable(Screen.Article.route) {
            ArticleScreen(navHostController = navController)
        }
        composable(
            route = Screen.DetailArticle.route,
            arguments = listOf(navArgument("articleId") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("articleId") ?: ""
            val articleId = id.toIntOrNull() ?: 0
            DetailArticleScreen(
                navHostController = navController,
                articleId = articleId
            )
        }
    }
}