package com.capstone.hidroqu.ui.screen.community

import android.content.Context
import android.util.Log
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.TopBarDefault
import com.capstone.hidroqu.nonui.data.PostData
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.component.CardCommunity
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.CommunityViewModel
import com.capstone.hidroqu.utils.formatDate
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CommunityActivity(
    navHostController: NavHostController,
    viewModel: CommunityViewModel = viewModel(),
    context: Context = LocalContext.current,
    onAddClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val communityPosts by viewModel.communityPosts.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState("")

    LaunchedEffect(token) {
        token?.let {
            viewModel.fetchAllCommunityPosts(it)
        } ?: run {
            navHostController.navigate(Screen.Login.route) {
                popUpTo(Screen.Community.route) { inclusive = true }
            }
        }
    }


    Scaffold(
        topBar = {
            TopBarDefault("Komunitas")
        },
        floatingActionButton = {
            AskButton(onClick = onAddClicked)
        },
        floatingActionButtonPosition = FabPosition.End
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
                NoPostList()
            }
        }
        else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                PostList(posts = communityPosts, onDetailClicked = { idPost ->
                    navHostController.navigate(Screen.DetailCommunity.createRoute(idPost)) {
                        popUpTo(Screen.Community.route)
                    }
                })
            }
        }
    }
}

@Composable
fun AskButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Add Post")
            Text(
                text = "Tanya Komunitas",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
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
            text = "Belum ada postingan",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tambahkan postingan pertama anda",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PostList(posts: List<PostData>, modifier: Modifier = Modifier, onDetailClicked: (Int) -> Unit) {
    // Sort posts first by page (desc), then by ID (desc) within each page
    val sortedPosts = posts.sortedWith(compareByDescending<PostData> { it.id })

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(20.dp)
    ) {
        sortedPosts.forEach { post ->
            Log.d("PostList", "Rendering post: ${post.user.name}, ID: ${post.id}")
            CardCommunity(
                listCommunity = post,
                onClick = {
                    onDetailClicked(post.id)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommunityActivityPreview() {
    HidroQuTheme {
        val navController = rememberNavController()
        CommunityActivity(
            navController,
            onAddClicked = {
            },
        )
    }
}
