package com.capstone.hidroqu.ui.component

import com.capstone.hidroqu.utils.ListDetailPostCommunity
import com.capstone.hidroqu.utils.dummyListDetailPostCommunity

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun CardPostComment(
    listComment: ListDetailPostCommunity,
    modifier: Modifier = Modifier,
) {
    // State to track whether the image is expanded or not
    val isImageExpanded = remember { mutableStateOf(false) }

    // Define the modifier for image size
    val imageModifier = if (isImageExpanded.value) {
        Modifier.wrapContentHeight() // Full height when expanded
    } else {
        Modifier.height(190.dp) // Limited height when not expanded
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(listComment.userCommentImg),
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
                    text = listComment.userCommentName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = listComment.timeComment,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        Text(
            text = listComment.txtComment,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

    }
}

@Preview
@Composable
private fun CardPostCommentPreview() {
    HidroQuTheme {
        dummyListDetailPostCommunity.forEach { comment ->
            CardPostComment (
                listComment = comment
            )
        }
    }
}