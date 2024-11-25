package com.capstone.hidroqu.ui.screen.profile

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.TopBarAction
import com.capstone.hidroqu.navigation.TopBarDefault
import com.capstone.hidroqu.nonui.data.User
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.utils.ListUserData
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.AuthViewModel
import com.capstone.hidroqu.ui.viewmodel.ProfileViewModel
import com.capstone.hidroqu.ui.viewmodel.ThemeViewModel
import com.capstone.hidroqu.utils.dummyListUserData

@Composable
fun ProfileActivity(
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

    // Dummy data for posts
    var visiblePostsCount by remember { mutableStateOf(5) }
    val dummyPosts = List(20) { "Post #${it + 1}" } // Dummy post titles
    val displayedPosts = dummyPosts.take(visiblePostsCount)
    val isMoreAvailable = visiblePostsCount < dummyPosts.size

    LaunchedEffect(token) {
        token?.let {
            profileViewModel.fetchUserProfile(it)
        }?: run {
            // Tangani kasus ketika token null, misalnya arahkan ke halaman login
            navHostController.navigate(Screen.Login.route) {
                popUpTo(Screen.Profile.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarAction(
                title = "Profil anda",
                navHostController = navHostController,
                onActionClick = {
                    authViewModel.logoutUser() // Panggil fungsi logout
                    navHostController.navigate(Screen.Login.route) { // Arahkan ke halaman login
                        popUpTo(Screen.Profile.route) { inclusive = true } // Bersihkan stack
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
            } else if (errorMessage.isNotEmpty()) {
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
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Profile Header
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        AsyncImage(
                            model = userData?.profile_image, // URL gambar dari userData
                            contentDescription = "Profile Icon",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            placeholder = painterResource(id = R.drawable.ic_launcher_background), // Placeholder jika gambar belum dimuat
                            error = painterResource(id = R.drawable.ic_launcher_background), // Gambar default jika terjadi kesalahan
                            contentScale = ContentScale.Crop // Sesuaikan gambar dengan crop
                        )

                        // Profile Name and Description
                        ProfileInfo(
                            name = userData?.name ?: "Nama pengguna",
                            bio = userData?.bio ?: "Bio belum ditambahkan"
                        )

                        // Edit Profile Button (Outlined)
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

                        // Appearance Settings (Theme change)
                        AppearanceSettings(
                            selectedMode = themeMode,
                            onModeChange = { newMode -> themeViewModel.setTheme(newMode.lowercase()) }
                        )

                        // My Posts Section
                        MyPostsSection(
                            posts = displayedPosts,
                            onLoadMore = { visiblePostsCount += 5 },
                            isMoreAvailable = isMoreAvailable
                        )
                    }
                }
            }
        }
    )
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
            style = MaterialTheme.typography.headlineLarge,
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
            color = MaterialTheme.colorScheme.onBackground // Appearance label color set to onBackground
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
    posts: List<String>,
    onLoadMore: () -> Unit,
    isMoreAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "My Posts",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            posts.forEach { post ->
                PostItem(postTitle = post)
            }

            // Load More Button
            if (isMoreAvailable) {
                Button(
                    onClick = onLoadMore,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Load More")
                }
            }
        }
    }
}


@Composable
fun PostItem(postTitle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate to post detail or edit */ },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = postTitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "View Post",
                tint = MaterialTheme.colorScheme.primary
            )
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
