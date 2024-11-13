package com.capstone.hidroqu.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.R
import com.capstone.hidroqu.component.CardAlarm
import com.capstone.hidroqu.component.CardArticle
import com.capstone.hidroqu.component.CardCamera

@Composable
fun HomeActivity(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopHome()
        CameraSection()
        AlarmSection()
        ArticleSection(navController)
    }
}

@Composable
fun TopHome(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Halo Tifah 👋",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Icon(
                Icons.Outlined.Notifications,
                contentDescription = "notification",
                modifier = Modifier.size(31.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "Tanamanmu kangen disapa nih!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}
@Composable
fun CameraSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp), // Padding sudah diatur di root
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CardCamera(
            modifier = Modifier.weight(0.5f),
            icon = R.drawable.camera,
            imageRes = R.drawable.poto_tanam,
            title = stringResource(R.string.txt_pototanam),
            description = stringResource(R.string.txt_dummy_pototanam),
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            borderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
            colorText = MaterialTheme.colorScheme.onTertiaryContainer
        )
        CardCamera(
            modifier = Modifier.weight(0.5f),
            icon = R.drawable.flip,
            imageRes = R.drawable.scan_tanam,
            title = stringResource(R.string.txt_scantanam),
            description = stringResource(R.string.txt_dummy_scantanam),
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            borderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            colorText = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun AlarmSection(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Alarm Panen",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Jangan lewatkan momen panenmu!",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp) // Jarak antar elemen dalam daftar
        ) {
            dummyListAlarmHome.forEach { alarm ->
                CardAlarm(alarm)
            }
        }
    }
}

@Composable
fun ArticleSection(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Artikel",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Temukan wawasan & tips terbaik untuk menanam",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Text(
                text = "Lihat semua",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 12.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate("Artikel") {
                        popUpTo("Home") {
                            // Menyimpan state HomeActivity
                            saveState = true
                        }
                        // Menggunakan launchSingleTop agar halaman Artikel tidak ditambahkan lagi ke stack
                        launchSingleTop = true
                        restoreState = true
                    }// Arahkan ke halaman artikel
                }
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp) // Jarak antar artikel
        ) {
            dummyListArticles.forEach { article ->
                CardArticle(
                    article = article,
                    onClick = {
                        navController.navigate("DetailArticle/${article.id}") {
                            popUpTo("Home") { // Bersihkan halaman Home dari stack
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
}


@Preview(device = Devices.DEFAULT, showBackground = true)
@Composable
fun HomeActivityPreview() {
    HidroQuTheme {
        val navController = rememberNavController()
        HomeActivity(navController)
    }
}
