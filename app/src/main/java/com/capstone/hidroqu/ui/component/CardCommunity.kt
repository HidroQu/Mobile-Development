package com.capstone.hidroqu.ui.component

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.capstone.hidroqu.R
import com.capstone.hidroqu.nonui.data.PostData
import com.capstone.hidroqu.utils.ListCommunity
import com.capstone.hidroqu.utils.dummyListCommunity
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.CommunityViewModel
import com.capstone.hidroqu.ui.viewmodel.ProfileViewModel
import com.capstone.hidroqu.utils.formatDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun CardCommunity(
    listCommunity: PostData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val isImageExpanded = remember { mutableStateOf(false) }

    val imageModifier = if (isImageExpanded.value) {
        Modifier.wrapContentHeight()
    } else {
        Modifier.height(190.dp)
    }

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Row untuk informasi pengguna
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()

            Image(
                painter = rememberAsyncImagePainter(
                    model = listCommunity.user.profile_image,
                    imageLoader = imageLoader
                ),
                modifier = Modifier
                    .size(50.dp) // Ukuran gambar
                    .clip(CircleShape) // Membuat gambar menjadi bulat
                    .border(
                        width = 2.dp, // Ketebalan border
                        color = MaterialTheme.colorScheme.outlineVariant, // Warna outline
                        shape = CircleShape // Bentuk border bulat
                    ),
                contentDescription = "Gambar Profil"
            )
            Column {
                Text(
                    text = listCommunity.user.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = formatDate(listCommunity.created_at),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        // Tampilkan judul dan konten
        Text(
            text = listCommunity.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Text(
            text = listCommunity.content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        // Periksa apakah gambar tersedia
        if (!listCommunity.image.isNullOrEmpty()) {
            val imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()

            Image(
                painter = rememberAsyncImagePainter(
                    model = listCommunity.image,
                    imageLoader = imageLoader
                ),
                contentDescription = "Image Post",
                modifier = imageModifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable {
                        // Toggle the image expanded state when clicked
                        isImageExpanded.value = !isImageExpanded.value
                    },
                contentScale = ContentScale.Crop
            )
        }

        // Bagian untuk komentar
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_comment),
                contentDescription = "Komentar",
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${listCommunity.comments_count} Jawaban",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = SemiBold,
                ),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}


@Preview
@Composable
private fun CardCommunityPreview() {
    HidroQuTheme {
//        dummyListCommunity.forEach { post ->
//            CardCommunity (
//                listCommunity = post,
//                onClick = {
//                    // Handle onClick here
//                }
//            )
//        }
    }
}
