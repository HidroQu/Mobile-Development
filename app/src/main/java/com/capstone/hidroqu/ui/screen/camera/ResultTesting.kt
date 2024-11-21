package com.capstone.hidroqu.ui.screen.camera

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

class ResultTesting : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResultScreen()
        }
    }
}

@Composable
fun ResultScreen() {
    val photoUri = (LocalContext.current as ResultTesting).intent.getStringExtra("photo_uri")
    if (photoUri != null) {
        AsyncImage(
            model = Uri.parse(photoUri),
            contentDescription = "Hasil Foto",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        Text("Tidak ada foto yang diambil", Modifier.padding(16.dp))
    }
}
