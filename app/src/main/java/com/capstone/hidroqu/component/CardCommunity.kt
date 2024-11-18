package com.capstone.hidroqu.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.list.ListCommunity
import com.capstone.hidroqu.ui.list.dummyListCommunity
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun CardCommunity(
    listCommunity: ListCommunity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
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
    ){
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(listCommunity.userPostImg),
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
                    text = listCommunity.userName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = listCommunity.time,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        Text(
            text = listCommunity.txtPost,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        // Image Post with conditional modifier for expanded state
        if (listCommunity.imgPost != null) {
            Image(
                painter = painterResource(id = listCommunity.imgPost),
                contentDescription = "Image Post",
                modifier = imageModifier
                    .fillMaxWidth()
                    .clickable {
                        // Toggle the image expanded state when clicked
                        isImageExpanded.value = !isImageExpanded.value
                    }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                painter = painterResource(R.drawable.ic_comment),
                contentDescription = "See Health Details",
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${listCommunity.commentList} Jawaban",
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
        dummyListCommunity.forEach { post ->
            CardCommunity (
                listCommunity = post,
                onClick = {
                    // Handle onClick here
                }
            )
        }
    }
}
