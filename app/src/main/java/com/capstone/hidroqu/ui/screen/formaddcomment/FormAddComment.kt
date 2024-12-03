package com.capstone.hidroqu.ui.screen.formaddcomment

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.TopBarButtonAction
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.CommunityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAddComment(
    navHostController: NavHostController,
    postId: Int,
    viewModel: CommunityViewModel = viewModel(),
    context: Context = LocalContext.current,
) {
    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    var commentText by remember { mutableStateOf("") }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // Add loading state

    val pickImages = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        if (uris.size > 1) {
            errorMessage = "Hanya boleh memilih satu foto."
        } else {
            imageUris = uris
            errorMessage = ""
        }
    }

    Scaffold(
        topBar = {
            TopBarButtonAction(
                title = "Komentar",
                navHostController = navHostController,
                onActionClick = {
                    if (commentText.isNotBlank()) {
                        if (token != null) {
                            isLoading = true // Show loading
                            viewModel.storeComment(
                                token = token!!,
                                communityId = postId,
                                content = commentText,
                                imageUri = imageUris.firstOrNull(),
                                context = context,
                                onSuccess = {
                                    Log.d("FormAddComment", "Comment berhasil ditambahkan")
                                    isLoading = false // Hide loading
                                    // Navigate back and refresh the comments
                                    navHostController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("refresh_comments", true)
                                    navHostController.popBackStack()
                                },
                                onError = { error ->
                                    isLoading = false // Hide loading
                                    errorMessage = error
                                    Log.e("FormAddComment", "Gagal menambahkan komentar: $error")
                                }
                            )
                        } else {
                            errorMessage = "Token tidak tersedia. Silakan login ulang."
                        }
                    } else {
                        errorMessage = "Komentar tidak boleh kosong."
                    }
                },
                actionText = "Kirim"
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
                TextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = {
                        Text(
                            "Tambahkan komentar Anda...",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

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
                                    .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
                            ) {
                                Image(
                                    painter = rememberImagePainter(imageUris[index]),
                                    contentDescription = "Selected Image $index",
                                    modifier = Modifier.fillMaxSize(),
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
}

@Preview(showBackground = true)
@Composable
private fun FormAddCommentPreview() {
    HidroQuTheme {
        val navHostController = rememberNavController()
        FormAddComment(navHostController = navHostController, postId = 1)
    }
}
