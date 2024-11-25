package com.capstone.hidroqu.ui.screen.article

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.TopBarAction
import com.capstone.hidroqu.nonui.data.ArticleDetailResponse
import com.capstone.hidroqu.nonui.data.PostData
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.component.CardArticle
import com.capstone.hidroqu.ui.screen.home.dummyListArticles
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.ArticleViewModel
import com.capstone.hidroqu.ui.viewmodel.CommunityViewModel

@Composable
fun ArticleActivity(
    navHostController: NavHostController,
    viewModel: ArticleViewModel = viewModel(),
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier
) {
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val articles by viewModel.articles.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState("")

    LaunchedEffect(token) {
        token?.let {
            viewModel.fetchAllArticles(it)
        } ?: run {
            // Tangani kasus ketika token null, misalnya arahkan ke halaman login
            navHostController.navigate(Screen.Login.route) {
                popUpTo(Screen.Community.route) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                if (!isSearchVisible) { // TopBar hanya muncul saat search tidak aktif
                    TopBarAction(
                        title = "Artikel",
                        navHostController = navHostController,
                        onActionClick = {
                            isSearchVisible = true
                        },
                        actionIcon = Icons.Default.Search
                    )
                }
            }
        ) { paddingValues ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (!errorMessage.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    NoPostList() // Assuming this is your error state or message
                }
            } else {
                // Display articles when not loading and no errors
                if (!isSearchVisible) {
                    Article(
                        articles = articles,
                        navHostController,
                        searchQuery,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
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
fun Article(articles: List<ArticleDetailResponse>, navHostController: NavController, searchQuery: String, modifier: Modifier = Modifier) {
    // Assuming articles is a list of ArticleDetailResponse fetched from the API
    val filteredArticles = remember(searchQuery) {
        articles.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.content.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        filteredArticles.forEach { article ->
            CardArticle(
                article = article,
                onClick = {
                    navHostController.navigate("DetailArticle/${article.id}") {
                        popUpTo("Artikel") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Composable
fun NoPostList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Belum ada artikel",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
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
