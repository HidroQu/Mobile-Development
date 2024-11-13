package com.capstone.hidroqu.ui.article

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.component.CardArticle
import com.capstone.hidroqu.ui.home.dummyListArticles
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun ArticleActivity(navController: NavHostController, modifier: Modifier = Modifier) {
    Column{
        TopBack(navController)
        Article(navController)
    }
}

@Composable
fun TopBack(navController: NavHostController, modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") } // State untuk menyimpan teks pencarian
    var isSearchActive by remember { mutableStateOf(false) } // State untuk menangani apakah search aktif

    // Kondisi untuk menampilkan search icon atau OutlinedTextField
    if (isSearchActive) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tampilkan OutlinedTextField ketika search aktif
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it }, // Menangani perubahan teks
                modifier = Modifier
                    .fillMaxWidth(1f), // Ukuran search bar lebih ramping
                shape = MaterialTheme.shapes.medium, // Membuat sudut lebih membulat
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search"
                    ) // Menambahkan ikon pencarian di kiri
                },
                label = null,
                singleLine = true, // Menjaga input hanya satu baris
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /* Handle keyboard done action */ }
                )
            )
        }
    }else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Artikel",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            // Tampilkan icon search jika search tidak aktif
            IconButton(onClick = { isSearchActive = true }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        }

    }
}


@Composable
fun Article(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp) // Jarak antar artikel
    ) {
        dummyListArticles.forEach { article ->
            CardArticle(
                article = article,
                onClick = {
                    navController.navigate("DetailArticle/${article.id}") {
                        popUpTo("Artikel") { // Bersihkan halaman Home dari stack
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(device = Devices.DEFAULT, showBackground = true)
@Composable
fun ArticleActivityPreview() {
    HidroQuTheme {
        val navController = rememberNavController()
        ArticleActivity(navController)
    }
}
