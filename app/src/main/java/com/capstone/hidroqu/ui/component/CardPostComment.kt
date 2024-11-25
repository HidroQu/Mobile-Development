package com.capstone.hidroqu.ui.component

import com.capstone.hidroqu.utils.ListDetailPostCommunity
import com.capstone.hidroqu.utils.dummyListDetailPostCommunity

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.capstone.hidroqu.nonui.data.Comment
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun CardPostComment(
    listComment: Comment,
    modifier: Modifier = Modifier,
) {
    val isImageExpanded = remember { mutableStateOf(false) }

    val imageModifier = if (isImageExpanded.value) {
        Modifier.wrapContentHeight()
    } else {
        Modifier.height(190.dp)
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = modifier
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
                    model = listComment.user.profile_image,
                    imageLoader = imageLoader
                ),
                contentDescription = "Profil",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = CircleShape
                    )
            )
            Column {
                Text(
                    text = listComment.user.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = listComment.created_at,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Text(
            text = listComment.content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        if (!listComment.image.isNullOrEmpty()) { // Menggunakan `listComment.image`
            val imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()

            Image(
                painter = rememberAsyncImagePainter(
                    model = listComment.image, // Pastikan `image` digunakan
                    imageLoader = imageLoader
                ),
                contentDescription = "Image Post",
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

@Preview
@Composable
private fun CardPostCommentPreview() {
    HidroQuTheme {
        dummyListDetailPostCommunity.forEach { comment ->
//            CardPostComment (
//                listComment = comment
//            )
        }
    }
}
