package com.capstone.hidroqu.ui.home

data class ListArticleHome(
    val id: Int, val title: String, val summary: String
)
val dummyListArticles = listOf(
    ListArticleHome(1, "Cara Menanam Bayam nomor 1", "Tips menanam bayam dengan mudah di rumah."),
    ListArticleHome(2, "Manfaat Tomat nomor 2", "Tomat memiliki banyak manfaat untuk kesehatan."),
    ListArticleHome(3, "Cara Menanam Bayam nomor 3", "Tips menanam bayam dengan mudah di rumah."),
    ListArticleHome(4, "Manfaat Tomat nomor 4", "Tomat memiliki banyak manfaat untuk kesehatan.")
)

// Fungsi untuk mengambil artikel berdasarkan ID
fun getArticleById(articleId: Int): ListArticleHome? {
    return dummyListArticles.find { it.id == articleId }
}