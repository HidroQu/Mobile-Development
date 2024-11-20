package com.capstone.hidroqu.ui.screen.detailcommunity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.navigation.TopBarDefault
import com.capstone.hidroqu.ui.component.CardPostComment
import com.capstone.hidroqu.utils.ListCommunity
import com.capstone.hidroqu.utils.ListDetailPostCommunity
import com.capstone.hidroqu.utils.getDetailPostById
import com.capstone.hidroqu.utils.getPostById
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun DetailPostCommunityActivity(
    navHostController: NavHostController,
    idPost: Int,
    modifier: Modifier = Modifier
) {
    val listComment = getDetailPostById(idPost)
    val post = getPostById(idPost)

    if (post != null) {
        DetailPostCommunityContent(post, listComment, navHostController)
    } else {
        Text("Postingan tidak ditemukan", style = MaterialTheme.typography.bodyLarge)
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailPostCommunityContent(
    post: ListCommunity,
    listComment: List<ListDetailPostCommunity>,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
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
                        Image(
                            painter = painterResource(post.userPostImg),
                            contentDescription = "Gambar Tanaman",
                            modifier = Modifier
                                .size(50.dp) // Ukuran gambar
                                .clip(CircleShape) // Membuat gambar menjadi bulat
                                .border(
                                    width = 2.dp, // Ketebalan border
                                    color = MaterialTheme.colorScheme.outlineVariant, // Warna outline
                                    shape = CircleShape // Bentuk border bulat
                                )
                        )
                        Column {
                            Text(
                                text = post.userName,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = post.time,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    Text(
                        text = post.txtPost,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    // Gambar Post dengan kondisi untuk tampilkan gambar jika ada
                    if (post.imgPost != null) {
                        Image(
                            painter = painterResource(id = post.imgPost),
                            contentDescription = "Image Post",
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                    }
                }

                // Menampilkan komentar
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
        topBar = {
            SimpleLightTopAppBar(
                "Postingan",
                navHostController = navHostController
            )
        },
        bottomBar = {
            // Bar bagian bawah untuk input komentar dan tombol kirim
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(end = 20.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // TextField untuk memasukkan komentar tanpa background dan transparan saat fokus
                TextField(
                    value = commentText.value,
                    onValueChange = { commentText.value = it },
                    label = { Text("Tambahkan komentar") },
                    modifier = Modifier
                        .weight(1f),
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        focusedIndicatorColor = Color.Transparent, // Menghilangkan warna indikator fokus
                        unfocusedIndicatorColor = Color.Transparent, // Menghilangkan warna indikator unfocused
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )

                // Tombol kirim
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