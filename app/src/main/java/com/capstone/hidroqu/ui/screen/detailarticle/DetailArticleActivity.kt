package com.capstone.hidroqu.ui.screen.detailarticle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.ui.screen.home.ListArticleHome
import com.capstone.hidroqu.ui.screen.home.getArticleById
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailArticleScreen(
    navController: NavHostController,
    articleId: Int
) {

    val article = getArticleById(articleId)

    if (article != null) {
        Scaffold(
            topBar = {
                SimpleLightTopAppBar(
                    title = article.title ?: "Detail Artikel", // Menampilkan judul artikel
                    navController = navController
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues) // Tambahkan padding dari Scaffold
                        .fillMaxSize()
                ) {
                    DetailArticleContent(
                        article = article,
                        modifier = Modifier.padding(paddingValues) // Gunakan paddingValues di sini
                    )
                }
            }
        )
    } else {
        Text("Artikel tidak ditemukan", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun DetailArticleContent(article: ListArticleHome, modifier: Modifier = Modifier) {
    Column {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Artikel",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text (
                    text = article.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Row (
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "07 November 2024",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "Penulis: Syakillah Nachwa",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            // Article content
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Preview(device = Devices.DEFAULT, showBackground = true)
@Composable
fun DetailArticleActivityPreview() {
    HidroQuTheme {
        val navController = rememberNavController() // Create a NavHostController
        DetailArticleScreen(navController = navController, articleId = 1) // Pass navController to the screen
    }
}
