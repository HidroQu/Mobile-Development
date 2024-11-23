package com.capstone.hidroqu.ui.screen.detailcommunity

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.navigation.TopBarDefault
import com.capstone.hidroqu.nonui.data.Comment
import com.capstone.hidroqu.nonui.data.CommunityDetailResponse
import com.capstone.hidroqu.nonui.data.DiagnosticHistory
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.component.CardPostComment
import com.capstone.hidroqu.ui.screen.detailmyplant.DetailMyPlantContent
import com.capstone.hidroqu.utils.ListCommunity
import com.capstone.hidroqu.utils.ListDetailPostCommunity
import com.capstone.hidroqu.utils.getDetailPostById
import com.capstone.hidroqu.utils.getPostById
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.CommunityViewModel
import com.capstone.hidroqu.ui.viewmodel.MyPlantViewModel

@Composable
fun DetailPostCommunityActivity(
    navHostController: NavHostController,
    idPost: Int,
    viewModel: CommunityViewModel = viewModel(),
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier
) {
    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val communityDetail by viewModel.communityDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessage by viewModel.errorMessage.collectAsState("")

    LaunchedEffect(idPost) {
        token?.let {
            Log.d("DetailPostCommunity", "Token ditemukan, memulai fetch detail komunitas")
            viewModel.fetchCommunityDetail(it, idPost)
        } ?: run {
            Log.e("DetailPostCommunity", "Token tidak ditemukan. Tidak dapat memuat detail komunitas.")
        }
    }

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(
                title = "Postingan",
                navHostController = navHostController
            )
        },
        content = { paddingValues ->
            if (isLoading) {
                Log.d("DetailPostCommunity", "Sedang memuat detail komunitas...")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (!errorMessage.isNullOrEmpty()) {
                Log.e("DetailPostCommunity", "Error: $errorMessage")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Postingan tidak ditemukan", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                Log.d("DetailPostCommunity", "Data berhasil dimuat: $communityDetail")
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    DetailPostCommunityContent(
                        post = communityDetail,
                        listComment = communityDetail?.get_comments ?: listOf(),
                        navHostController = navHostController
                    )
                }
            }

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailPostCommunityContent(
    post: CommunityDetailResponse?,
    listComment: List<Comment>,
    context: Context = LocalContext.current,
    navHostController: NavHostController
) {
    // State untuk teks komentar
    val commentText = remember { mutableStateOf("") }

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(paddingValues),
            ) {
                // Konten utama yang sudah ada (gambar, teks, dll)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val imageLoader = ImageLoader.Builder(LocalContext.current)
                            .components {
                                add(SvgDecoder.Factory())
                            }
                            .build()

                        if (post != null) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = post.image,
                                    imageLoader = imageLoader
                                ),
                                contentDescription = "post imagen",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant,
                                        shape = CircleShape
                                    )
                            )
                        }
                        Column {
                            Text(
                                text = post?.user?.name ?: "Username",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text =  post?.created_at ?: "00/00/0000",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    Text(
                        text =  post?.content ?: "Lorem ipsum",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    listComment.forEach { comment ->
                        CardPostComment(
                            listComment = comment
                        )
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(end = 20.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = commentText.value,
                    onValueChange = { commentText.value = it },
                    label = { Text("Tambahkan komentar") },
                    modifier = Modifier
                        .weight(1f),
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )

                Button(
                    onClick = {
                        // Logika untuk mengirim komentar
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("Kirim")
                }
            }
        }
    )
}

@Preview
@Composable
private fun DetailPostCommunityPreview() {
    val navHostController = rememberNavController()
    HidroQuTheme {
        DetailPostCommunityActivity(navHostController , 4)
    }
}