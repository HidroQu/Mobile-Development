package com.capstone.hidroqu.ui.screen.profile

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.TopBarDefaultAction
import com.capstone.hidroqu.nonui.data.MyPostData
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.component.CardMyPost
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.AuthViewModel
import com.capstone.hidroqu.ui.viewmodel.ProfileViewModel
import com.capstone.hidroqu.ui.viewmodel.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    themeViewModel: ThemeViewModel,
    profileViewModel: ProfileViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    context: Context = LocalContext.current,
) {
    val themeMode by themeViewModel.themeMode.collectAsState()

    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val userData by profileViewModel.userData.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val errorMessage by profileViewModel.errorMessage.collectAsState()

    LaunchedEffect(token) {
        token?.let {
            profileViewModel.fetchUserProfile(it)
            profileViewModel.getMyPost(it)
        } ?: run {
            navHostController.navigate(Screen.AuthRoute.route) {
                popUpTo(Screen.ProfileRoute.route) { inclusive = true }
            }
        }
    }
    val myPosts by profileViewModel.myPost.collectAsState()
    var visiblePostsCount by remember { mutableStateOf(5) }
    val displayedPosts = myPosts.take(visiblePostsCount)
    val isMoreAvailable = visiblePostsCount < myPosts.size

    LaunchedEffect(token) {
        token?.let {
            profileViewModel.fetchUserProfile(it)
        }?: run {
            navHostController.navigate(Screen.Login.route) {
                popUpTo(Screen.Profile.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarDefaultAction(
                title = "Profil anda",
                navHostController = navHostController,
                onActionClick = {
                    authViewModel.logoutUser()
                    navHostController.navigate(Screen.AuthRoute.route) {
                        popUpTo(Screen.ProfileRoute.route) { inclusive = true }
                    }
                },
                actionIcon = Icons.Default.ExitToApp )
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        AsyncImage(
                            model = userData?.photo,
                            contentDescription = "Profile Icon",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                            error = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentScale = ContentScale.Crop
                        )
                        ProfileInfo(
                            name = userData?.name ?: "Nama pengguna",
                            bio = userData?.bio ?: "Bio belum ditambahkan"
                        )
                        OutlinedButton(
                            onClick = { navHostController.navigate(Screen.EditProfile.route) },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = "Edit Profil",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        AppearanceSettings(
                            selectedMode = themeMode,
                            onModeChange = { newMode -> themeViewModel.setTheme(newMode.lowercase()) }
                        )
                        if (myPosts.isEmpty()) {
                            NoPostList(modifier = Modifier)
                        } else {
                            MyPostsSection(
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
fun NoPostList(
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
                painter = painterResource(id = R.drawable.no_post),
                contentDescription = "no post",
                modifier = Modifier
                    .size(50.dp)
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
fun ProfileInfo(
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
fun AppearanceSettings(
    selectedMode: String,
    onModeChange: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            "Appearance",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        AppearanceOption("System", selected = selectedMode == "system", onClick = { onModeChange("system") })
        AppearanceOption("Light", selected = selectedMode == "light", onClick = { onModeChange("light") })
        AppearanceOption("Dark", selected = selectedMode == "dark", onClick = { onModeChange("dark") })
    }
}

@Composable
fun AppearanceOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.outline
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .clickable { onClick() }
        )
    }
}

@Composable
fun MyPostsSection(
    posts: List<MyPostData>,
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
            text = "Postinganku",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sortedPosts.forEach { post ->
                CardMyPost(
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
