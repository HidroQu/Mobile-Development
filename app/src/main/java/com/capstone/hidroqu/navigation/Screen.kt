package com.capstone.hidroqu.navigation

sealed class Screen(val route: String) {

    //auth
    object Login : Screen("login")
    object Register : Screen("register")

    //camera
    object CameraPotoTanam : Screen("camerapototanam")
    object CameraScanTanam : Screen("camerascantanam")

    //main
    object Home : Screen("home")
    object MyPlant : Screen("myplant")
    object Community : Screen("community")
    object Profile : Screen("profile")

    //artikel
    object Article : Screen("article")
    object DetailArticle : Screen("DetailArticle/{articleId}") {
        fun createRoute(articleId: Int) = "DetailArticle/$articleId"
    }

    //tanamanku section
    object DetailMyPlant : Screen("DetailMyPlant/{plantId}") {
        fun createRoute(plantId: Int) = "DetailMyPlant/$plantId"
    }

    object AddPlant : Screen("addplant")
    object FormAddPlant : Screen("FormTanaman/{plantId}") {
        fun createRoute(plantId: Int) = "FormTanaman/$plantId"
    }

    //komunitas
    object DetailCommunity : Screen("DetailCommunity/{postId}") {
        fun createRoute(postId: Int) = "DetailCommunity/$postId"
    }

    object AddPostCommunity : Screen("addpostcommunity")

    //profile
    object EditProfile : Screen("editprofile")
}