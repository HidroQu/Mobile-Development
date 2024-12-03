package com.capstone.hidroqu.navigation

import com.capstone.hidroqu.nonui.data.PlantResponse

sealed class Screen(val route: String) {

    //auth
    object AuthRoute : Screen ("authroute")
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot")
    object ResetPassword : Screen("resetpassword")

    //home
    object HomeRoute : Screen ("homeroute")
    object Home : Screen("home")
    object CameraPotoTanam : Screen("camerapototanam")
    object CameraScanTanam : Screen("camerascantanam")

    object ResultPotoTanam : Screen("ResultPotoTanam/{photoUri}") {
        fun createRoute(photoUri: String) = "ResultPotoTanam/$photoUri"
    }
    object ResultScanTanam : Screen("ResultScanTanam/{photoUri}") {
        fun createRoute(photoUri: String) = "ResultScanTanam/$photoUri"
    }
    object ChoosePlant : Screen("ChoosePlant/{diagnoseId}/{photoUri}") {
        fun createRoute(diagnoseId: Int, photoUri: String) = "ChoosePlant/$diagnoseId/$photoUri"
    }
    //artikel
    object Article : Screen("article")
    object DetailArticle : Screen("DetailArticle/{articleId}") {
        fun createRoute(articleId: Int) = "DetailArticle/$articleId"
    }

    //tanamanku section
    object MyPlantRoute : Screen ("myplantroute")
    object MyPlant : Screen("myplant")
    object DetailMyPlant : Screen("DetailMyPlant/{plantId}") {
        fun createRoute(plantId: Int) = "DetailMyPlant/$plantId"
    }
    //riwayat
    object HistoryMyPlant : Screen("HistoryMyPlant/{plantId}/{healthId}") {
        fun createRoute(plantId: Int, healthId: Int) = "HistoryMyPlant/$plantId/$healthId"
    }
    object AddPlant : Screen("addplant")
    object FormAddPlant : Screen("FormTanaman/{plantId}") {
        fun createRoute(plantId: Int) = "FormTanaman/$plantId"
    }


    //komunitas
    object CommunityRoute : Screen("communityroute")
    object Community : Screen("community")
    object DetailCommunity : Screen("DetailCommunity/{idPost}") {
        fun createRoute(idPost: Int) = "DetailCommunity/$idPost"
    }

    object AddPostCommunity : Screen("addpostcommunity")
    object AddPostComment : Screen("AddComment/{postId}") {
        fun createRoute(postId: Int) = "AddComment/$postId"
    }

    //profile
    object ProfileRoute : Screen("profileroute")
    object Profile : Screen("profile")
    object EditProfile : Screen("editprofile")
}