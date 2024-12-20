package com.capstone.hidroqu.ui.screen.detailcommunity

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.capstone.hidroqu.nonui.data.Comment
import com.capstone.hidroqu.nonui.data.CommunityDetailResponse
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.component.CardPostComment
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.CommunityViewModel
import com.capstone.hidroqu.utils.formatDate

@Composable
fun DetailPostCommunityScreen(
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
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val shouldRefresh = currentBackStackEntry?.savedStateHandle?.get<Boolean>("refresh_comments") ?: false
    val isSubmittingComment = remember { mutableStateOf(false) }
    val commentSubmissionError = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(idPost, shouldRefresh) {
        if (shouldRefresh) {
            currentBackStackEntry?.savedStateHandle?.remove<Boolean>("refresh_comments")
            token?.let {
                viewModel.fetchCommunityDetail(it, idPost)
            }
        }
    }

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
                        navHostController = navHostController,
                        token = token,
                        viewModel = viewModel,
                        isSubmittingComment = isSubmittingComment,
                        commentSubmissionError = commentSubmissionError,
                        idPost = idPost
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
    navHostController: NavHostController,
    token: String?,
    idPost: Int,
    viewModel: CommunityViewModel,
    isSubmittingComment: MutableState<Boolean>,
    commentSubmissionError: MutableState<String?>
) {
    val isImageExpanded = remember { mutableStateOf(false) }
    val commentText = remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val isContentExpanded = remember { mutableStateOf(false) }

    val pickImages = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        if (uris.size > 1) {
            errorMessage = "Hanya boleh memilih satu foto."
        } else {
            imageUris = uris
            errorMessage = ""
        }
    }

    val imageModifier = if (isImageExpanded.value) {
        Modifier.wrapContentHeight()
    } else {
        Modifier.height(190.dp)
    }

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(paddingValues),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        val imageLoader = ImageLoader.Builder(LocalContext.current)
                            .components {
                                add(SvgDecoder.Factory())
                            }
                            .build()

                        if (post != null) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = post.user.photo,
                                    imageLoader = imageLoader,
                                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                                    error = painterResource(id = R.drawable.ic_launcher_foreground),
                                ),
                                contentDescription = "User Profile Image",
                                modifier = Modifier
                                    .clickable {
                                        navHostController.navigate(Screen.ProfileOther.createRoute(idPost))
                                    }
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant,
                                        shape = CircleShape
                                    ),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = post?.user?.name ?: "Username",
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = formatDate(post?.created_at ?: "00/00/0000"),
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.wrapContentWidth(Alignment.End),
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                Text(
                                    text = post?.title ?: "Lorem ipsum",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Text(
                                text = post?.content ?: "Lorem ipsum",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                maxLines = if (isContentExpanded.value) Int.MAX_VALUE else 10,
                                overflow = TextOverflow.Ellipsis
                            )
                            if ((post?.content ?: "").length > 150) {
                                Text(
                                    text = if (isContentExpanded.value) "Tampilkan lebih sedikit" else "Tampilkan lebih banyak",
                                    modifier = Modifier
                                        .clickable { isContentExpanded.value = !isContentExpanded.value }
                                        .padding(top = 8.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (!post?.image.isNullOrEmpty()) {
                                Image(
                                    painter = rememberAsyncImagePainter(post?.image),
                                    contentDescription = "Post Image",
                                    modifier = imageModifier
                                        .fillMaxWidth()
                                        .clickable {
                                            isImageExpanded.value = !isImageExpanded.value
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    if (isSubmittingComment.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    commentSubmissionError.value?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    listComment.forEach { comment ->
                        CardPostComment(listComment = comment, post = post, navHostController = navHostController)
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val commentText = remember { mutableStateOf("") }

                Icon(
                    painter = painterResource(R.drawable.ic_gallery),
                    contentDescription = "Galeri",
                    modifier = Modifier.clickable {
                        navHostController.navigate(Screen.AddPostComment.createRoute(post?.id ?: 0))
                    },
                    tint = MaterialTheme.colorScheme.primary
                )

                TextField(
                    value = commentText.value,
                    onValueChange = { commentText.value = it },
                    label = { Text("Tambahkan komentar") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            navHostController.navigate(Screen.AddPostComment.createRoute(post?.id ?: 0))
                        },

                    maxLines = 2,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !isSubmittingComment.value
                )

                Button(
                    onClick = {
                        if (token != null && post != null && commentText.value.isNotBlank()) {
                            isSubmittingComment.value = true
                            commentSubmissionError.value = null

                            viewModel.storeComment(
                                token = token,
                                communityId = post.id,
                                content = commentText.value,
                                imageUri = null,
                                context = context,
                                onSuccess = { response ->
                                    isSubmittingComment.value = false
                                    commentText.value = ""
                                    viewModel.fetchCommunityDetail(token, post.id)
                                },
                                onError = { error ->
                                    isSubmittingComment.value = false
                                    commentSubmissionError.value = error
                                }
                            )
                        }
                    },
                    enabled = !isSubmittingComment.value && commentText.value.isNotBlank(),
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
        DetailPostCommunityScreen(navHostController , 4)
    }
}