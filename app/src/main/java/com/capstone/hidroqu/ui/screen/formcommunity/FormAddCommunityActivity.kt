package com.capstone.hidroqu.ui.screen.formcommunity

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.TopBarButtonAction
import com.capstone.hidroqu.nonui.data.PostData
import com.capstone.hidroqu.nonui.data.UserData
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.screen.profile.ProfileInfo
import com.capstone.hidroqu.utils.ListUserData
import com.capstone.hidroqu.utils.dummyListUserData
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.CommunityViewModel
import com.capstone.hidroqu.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAddCommunityActivity(
    navHostController: NavHostController,
    context: Context = LocalContext.current,
    profileViewModel: ProfileViewModel = viewModel(),
    viewModel: CommunityViewModel = viewModel(),
) {
    val userData by profileViewModel.userData.collectAsState()

    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val communityPosts by viewModel.communityPosts.collectAsState(emptyList())
    var postTittle by remember { mutableStateOf("") }
    var postText by remember { mutableStateOf("") }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isImageExpanded by remember { mutableStateOf(false) }
    var expandedImageUri by remember { mutableStateOf<Uri?>(null) }
    var scale by remember { mutableStateOf(1f) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val pickImages = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        if (uris.size > 1) {
            errorMessage = "Hanya boleh memilih satu foto."
        } else {
            imageUris = uris
            errorMessage = ""
        }
    }

    LaunchedEffect(Unit) {
        token?.let {
            profileViewModel.fetchUserProfile(it)
            viewModel.fetchAllCommunityPosts(it)
        } ?: Log.e("CommunityActivity", "Token is null. Redirect to login.")
    }

    LaunchedEffect(token) {
        token?.let {
            profileViewModel.fetchUserProfile(it)
        }?: run {
            navHostController.navigate(Screen.AuthRoute.route) {
                popUpTo(Screen.Profile.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarButtonAction(
                title = "Tambah Postingan",
                navHostController = navHostController,
                onActionClick = {
                    if (postTittle.isNotBlank() && postText.isNotBlank()) {

                        if (token != null) {
                            viewModel.storePost(
                                token = token!!,
                                title = postTittle,
                                content = postText,
                                imageUri = imageUris.firstOrNull(),
                                context = context,
                                onSuccess = { response ->
                                    Log.d("FormAddCommunityActivity", "Post added successfully: ${response.message}")
                                    navHostController.popBackStack()
                                },
                                onError = { error ->
                                    errorMessage = error
                                    Log.e("FormAddCommunityActivity", "Failed to add post: $error")
                                }
                            )
                        } else {
                            errorMessage = "Token tidak tersedia. Silakan login ulang."
                        }
                    } else {
                        errorMessage = "Judul dan Konten tidak boleh kosong."
                    }
                },
                actionText = "Posting"
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AsyncImage(
                            model = userData?.photo ?: "",
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = CircleShape
                                ),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = userData?.name ?: "Nama pengguna",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                }

                TextField(
                    value = postTittle,
                    onValueChange = { postTittle = it },
                    placeholder = {
                        Text(
                            "Masukkan Judul",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                TextField(
                    value = postText,
                    onValueChange = { postText = it },
                    placeholder = {
                        Text(
                            "Tanyakan sesuatu di komunitas",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                if (imageUris.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(imageUris.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(250.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        expandedImageUri = imageUris[index]
                                        isImageExpanded = true
                                    }
                            ) {
                                Image(
                                    painter = rememberImagePainter(imageUris[index]),
                                    contentDescription = "Selected Image $index",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                IconButton(
                                    onClick = {
                                        imageUris = imageUris.filterIndexed { i, _ -> i != index }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Image",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { pickImages.launch("image/*") },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_gallery),
                        contentDescription = "Galeri",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Tambahkan Gambar",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    )

    if (isImageExpanded && expandedImageUri != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable {
                    isImageExpanded = false
                }
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, _, zoom, _ ->
                            scale *= zoom
                            scale = scale.coerceIn(1f, 3f)
                        }
                    }
            ) {
                Image(
                    painter = rememberImagePainter(expandedImageUri),
                    contentDescription = "Expanded Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale
                        )
                )
            }
        }
    }
}

@Composable
fun UserItem(
    name: String
) {

}

@Preview(showBackground = true)
@Composable
private fun FormAddCommunityActivityPreview() {
    HidroQuTheme {
        val navHostController = rememberNavController()
        FormAddCommunityActivity(navHostController = navHostController)
    }
}
