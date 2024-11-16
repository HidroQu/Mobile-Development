package com.capstone.hidroqu.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.home.ListArticleHome

@Composable
fun CardArticle(article: ListArticleHome, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = MaterialTheme.shapes.medium
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Artikel",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Column (
            modifier = modifier
                .padding(16.dp)
        ){
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = article.summary,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}