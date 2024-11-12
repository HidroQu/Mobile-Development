package com.capstone.hydroqu.ui.home

data class ListArticleHome(
    val title: String, val summary: String
)
val dummyListArticles = listOf(
    ListArticleHome("Cara Menanam Bayam", "Tips menanam bayam dengan mudah di rumah."),
    ListArticleHome("Manfaat Tomat", "Tomat memiliki banyak manfaat untuk kesehatan.")
)