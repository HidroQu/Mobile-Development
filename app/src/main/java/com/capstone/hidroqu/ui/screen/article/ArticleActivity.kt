package com.capstone.hidroqu.ui.screen.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.ui.component.CardArticle
import com.capstone.hidroqu.ui.screen.home.dummyListArticles
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun ArticleActivity(navController: NavHostController, searchQuery: String, modifier: Modifier = Modifier) {
    Column {
        Article(navController, searchQuery)  // Pass searchQuery to filter
    }
}

@Composable
fun Article(navController: NavHostController, searchQuery: String, modifier: Modifier = Modifier) {
    val filteredArticles = dummyListArticles.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.summary.contains(searchQuery, ignoreCase = true)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        filteredArticles.forEach { article ->
            CardArticle(
                article = article,
                onClick = {
                    navController.navigate("DetailArticle/${article.id}") {
                        popUpTo("Artikel") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(device = Devices.DEFAULT, showBackground = true)
@Composable
fun ArticleActivityPreview() {
    HidroQuTheme {
        val navController = rememberNavController()
        ArticleActivity(navController, "")
    }
}
