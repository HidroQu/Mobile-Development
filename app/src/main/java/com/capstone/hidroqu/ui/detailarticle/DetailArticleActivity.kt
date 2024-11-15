package com.capstone.hidroqu.ui.detailarticle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.home.ListArticleHome
import com.capstone.hidroqu.ui.home.getArticleById
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun DetailArticleActivity(articleId: Int, modifier: Modifier = Modifier) {
    val article = getArticleById(articleId)

    // Menampilkan konten hanya jika artikel ditemukan
    if (article != null) {
        Column {
            DetailArticleContent(article)
        }
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
        DetailArticleActivity(1)
    }
}
