package com.capstone.hidroqu.ui.screen.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.ui.component.CardArticle
import com.capstone.hidroqu.ui.screen.home.dummyListArticles
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleActivity(navHostController: NavHostController, modifier: Modifier = Modifier) {
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                if (!isSearchVisible) { // TopBar hanya muncul saat search tidak aktif
                    TopAppBar(
                        title = { Text("Artikel") },
                        actions = {
                            IconButton(onClick = { isSearchVisible = true }) {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        ) { paddingValues ->
            if (!isSearchVisible) {
                Article(navHostController, searchQuery, Modifier.padding(paddingValues))
            }
        }

        // SearchBarScreen akan menutupi seluruh layar saat search aktif
        if (isSearchVisible) {
            SearchBarScreen(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it },
                onClose = { isSearchVisible = false },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarScreen(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = onQueryChange,
            onSearch = { onClose() },
            active = true,
            onActiveChange = {},
            placeholder = {
                Text(
                    text = "Cari artikel atau informasi...",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        onQueryChange("")
                        onClose()
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon",
                    tint = MaterialTheme.colorScheme.error
                )
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            content = {}
        )
    }
}


@Composable
fun Article(navHostController: NavController, searchQuery: String, modifier: Modifier = Modifier) {
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
                    navHostController.navigate("DetailArticle/${article.id}") {
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
        val navHostController = rememberNavController()
        ArticleActivity(navHostController)
    }
}
