package com.capstone.hidroqu.ui.screen.formcommunity

import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.navigation.TopBarAction
import com.capstone.hidroqu.navigation.TopBarButtonAction
import com.capstone.hidroqu.utils.ListUserData
import com.capstone.hidroqu.utils.dummyListUserData
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAddCommunityActivity(
    navHostController: NavHostController
) {
    val user = dummyListUserData[0]
    var postText by remember { mutableStateOf("") } // State for the post text
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) } // State for the selected images
    var isImageExpanded by remember { mutableStateOf(false) } // State to track if the image is expanded
    var expandedImageUri by remember { mutableStateOf<Uri?>(null) } // Store the URI of the expanded image
    var scale by remember { mutableStateOf(1f) } // Initial scale for zoom

    val context = LocalContext.current
    val pickImages = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        imageUris = uris
    }

    Scaffold(
        topBar = {
            TopBarButtonAction(
                title = "Tambah Postingan",
                navHostController = navHostController,
                onActionClick = {
                    //logika menyimpan ke database disini

                    navHostController.popBackStack()
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
                // Display user profile
                UserItem(user = user)

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
                        .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        focusedIndicatorColor = Color.Transparent,  // Remove the bottom focus indicator
                        unfocusedIndicatorColor = Color.Transparent // Remove the bottom unfocused indicator
                    )
                )

                // Show the selected images if available
                if (imageUris.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(imageUris.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(250.dp) // Memperbesar ukuran gambar menjadi 250x250 dp
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
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
                                        .clip(RoundedCornerShape(16.dp))
                                )

                                // Icon to delete the image
                                IconButton(
                                    onClick = {
                                        // Remove the image from the list when the icon is clicked
                                        imageUris = imageUris.filterIndexed { i, _ -> i != index }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp) // Adjust padding to move the icon inside the image box
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Image",
                                        tint = MaterialTheme.colorScheme.error // Use error color for delete icon
                                    )
                                }
                            }
                        }
                    }
                }

                // Button to select images
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
                        tint = MaterialTheme.colorScheme.primary // Use MaterialTheme color for icon
                    )
                    Text(
                        "Tambahkan Gambar",
                        style = MaterialTheme.typography.titleMedium, // Use appropriate typography
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Display the text typed by the user
                Text(
                    text = postText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )

    // Show the expanded image if isImageExpanded is true
    if (isImageExpanded && expandedImageUri != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)) // Semi-transparent background
                .clickable {
                    // Close the expanded image on click
                    isImageExpanded = false
                }
        ) {
            // Detect zoom gestures (pinch to zoom)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale *= zoom
                            scale = scale.coerceIn(1f, 3f) // Limit the zoom scale between 1x and 3x
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
fun UserItem(user: ListUserData) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Display user profile image
        Image(
            painter = painterResource(id = user.img),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(50.dp) // Ukuran gambar
                .clip(CircleShape) // Membuat gambar menjadi bulat
                .border(
                    width = 2.dp, // Ketebalan border
                    color = MaterialTheme.colorScheme.outlineVariant, // Warna outline
                    shape = CircleShape // Bentuk border bulat
                ) // Adjusted to match the design
        )
        Text(
            text = user.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormAddCommunityActivityPreview() {
    HidroQuTheme {
        val navHostController = rememberNavController()
        FormAddCommunityActivity(navHostController = navHostController)
    }
}
