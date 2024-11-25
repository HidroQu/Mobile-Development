package com.capstone.hidroqu.ui.screen.detailarticle

import android.content.Context
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.nonui.data.ArticleDetailResponse
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.screen.home.ListArticleHome
import com.capstone.hidroqu.ui.screen.home.getArticleById
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.ArticleViewModel
import com.capstone.hidroqu.ui.viewmodel.CommunityViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailArticleScreen(
    navHostController: NavHostController,
    articleId: Int,
    viewModel: ArticleViewModel = viewModel(),
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier
) {

    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val articleDetail by viewModel.articleDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState("")
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val shouldRefresh = currentBackStackEntry?.savedStateHandle?.get<Boolean>("refresh_comments") ?: false

    LaunchedEffect(articleId, shouldRefresh) {
        if (shouldRefresh) {
            currentBackStackEntry?.savedStateHandle?.remove<Boolean>("refresh_comments")
            token?.let {
                viewModel.fetchArticleDetail(it, articleId)
            }
        }
    }

    LaunchedEffect(articleId) {
        token?.let {
            Log.d("DetailPostCommunity", "Token ditemukan, memulai fetch detail komunitas")
            viewModel.fetchArticleDetail(it, articleId)
        } ?: run {
            Log.e("DetailPostCommunity", "Token tidak ditemukan. Tidak dapat memuat detail komunitas.")
        }
    }

    if (articleDetail != null) {
        Scaffold(
            topBar = {
                SimpleLightTopAppBar(
                    title = articleDetail?.title?: "Judul", // Menampilkan judul artikel
                    navHostController = navHostController
                )
            },
            content = { paddingValues ->

                    DetailArticleContent(
                        article = articleDetail,
                        modifier = Modifier.padding(paddingValues) // Gunakan paddingValues di sini
                    )

            }
        )
    } else {
        Text("Artikel tidak ditemukan", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun DetailArticleContent(
    article: ArticleDetailResponse?,
    modifier: Modifier = Modifier) {
    Column{
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()

        val painter = if (!article?.image.isNullOrEmpty()) {
            rememberAsyncImagePainter(
                model = article?.image,
                imageLoader = imageLoader
            )
        } else {
            painterResource(id = R.drawable.ic_launcher_background)
        }
        Image(
            painter = painter,
            contentDescription = "Artikel",
            modifier = modifier
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
                    text = article?.title?: "Judul",
                    style = MaterialTheme.typography.titleLarge
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = article?.updatedAt?: "00/00/0000",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = article?.user?.name?: "00/00/0000",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Text(
                text = article?.content?: "Lorem ipsum",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Preview(device = Devices.DEFAULT, showBackground = true)
@Composable
fun DetailArticleActivityPreview() {
    HidroQuTheme {
        val navHostController = rememberNavController() // Create a NavHostController
        DetailArticleScreen(navHostController = navHostController, articleId = 1) // Pass navController to the screen
    }
}
