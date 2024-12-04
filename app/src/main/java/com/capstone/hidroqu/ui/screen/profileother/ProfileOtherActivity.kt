package com.capstone.hidroqu.ui.screen.profileother

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.navigation.TopBarDefault
import com.capstone.hidroqu.navigation.TopBarDefaultAction
import com.capstone.hidroqu.nonui.data.MyPostData
import com.capstone.hidroqu.nonui.data.PostData
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.component.CardMyPost
import com.capstone.hidroqu.ui.component.CardOtherPost
import com.capstone.hidroqu.ui.screen.profile.MyPostsSection
import com.capstone.hidroqu.ui.screen.profile.ProfileInfo
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.AuthViewModel
import com.capstone.hidroqu.ui.viewmodel.CommunityViewModel
import com.capstone.hidroqu.ui.viewmodel.ProfileViewModel
import com.capstone.hidroqu.ui.viewmodel.ThemeViewModel
import com.capstone.hidroqu.utils.dummyListUserData
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProfileOtherActivity(
    navHostController: NavHostController,
    idPost: Int,
    viewModel: CommunityViewModel = viewModel(),
    context: Context = LocalContext.current,
) {
    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val userOtherData by viewModel.communityDetail.collectAsState()
    val filteredPosts by viewModel.communityPosts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(idPost, token) {
        token?.let {
            viewModel.fetchCommunityDetail(it, idPost)
            viewModel.fetchAllCommunityPosts(it)
        } ?: run {
            navHostController.navigate(Screen.AuthRoute.route) {
                popUpTo(Screen.CommunityRoute.route) { inclusive = true }
            }
        }
    }
    Log.d("ID", "ID USER${userOtherData?.id} ID USER2 ${userOtherData?.user?.id} ID USER2 ${userOtherData?.user_id}Sorted posts: ${filteredPosts.size}")
    val filteredPostsByUser = filteredPosts.filter { it.user_id == userOtherData?.user?.id }
    // Dummy data for posts
    var visiblePostsCount by remember { mutableStateOf(5) }
    val displayedPosts = filteredPostsByUser.take(visiblePostsCount)
    val isMoreAvailable = visiblePostsCount < filteredPostsByUser.size

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(
                title = "",
                navHostController = navHostController
            )
        },
        content = { paddingValues ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage?.isNotEmpty() == true) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = errorMessage ?: "Unknown Error", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Profile Header
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        AsyncImage(
                            model = userOtherData?.user?.photo,
                            contentDescription = "Profile Icon",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            placeholder = painterResource(id = R.drawable.ic_launcher_foreground), // Placeholder jika gambar belum dimuat
                            error = painterResource(id = R.drawable.ic_launcher_foreground), // Gambar default jika terjadi kesalahan
                            contentScale = ContentScale.Crop // Sesuaikan gambar dengan crop
                        )

                        // Profile Name and Description
                        ProfileOtherInfo(
                            name = userOtherData?.user?.name ?: "Nama pengguna",
                            bio = userOtherData?.user?.bio ?: "Bio belum ditambahkan"
                        )

                        // Other Posts Section
                        if (filteredPosts.isEmpty()) {
                            NoOtherPostList(modifier = Modifier)
                        } else {
                            OtherPostsSection(
                                posts = displayedPosts,
                                onLoadMore = { visiblePostsCount += 5 },
                                isMoreAvailable = isMoreAvailable,
                                onDetailClicked = { idPost ->
                                    navHostController.navigate(Screen.DetailCommunity.createRoute(idPost))
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun NoOtherPostList(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pupuk),
                contentDescription = "no post",
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Anda belum memposting apapun",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
fun ProfileOtherInfo(
    name: String,
    bio: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 28.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Text(
            text = bio,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OtherPostsSection(
    posts: List<PostData>,
    onLoadMore: () -> Unit,
    isMoreAvailable: Boolean,
    modifier: Modifier = Modifier,
    onDetailClicked: (Int) -> Unit
) {

    val sortedPosts = remember(posts) {
        posts.sortedByDescending {
            try {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
                    .parse(it.created_at)?.time ?: 0
            } catch (e: Exception) {
                0
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Postingan",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sortedPosts.forEach { post ->
                CardOtherPost(
                    listCommunity = post,
                    onClick = {
                        onDetailClicked(post.id)
                    }
                )
            }
            if (isMoreAvailable) {
                Button(
                    onClick = onLoadMore,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Lihat yang lain")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    HidroQuTheme {
        val navController = rememberNavController()
        val userData = dummyListUserData.first()
    }
}
