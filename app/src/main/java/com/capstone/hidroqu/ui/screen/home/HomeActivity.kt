package com.capstone.hidroqu.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.component.CardAlarm
import com.capstone.hidroqu.ui.component.CardArticle
import com.capstone.hidroqu.ui.component.CardCamera
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.viewmodel.HomeViewModel


@Composable
fun HomeActivity(navHostController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val homeViewModel = HomeViewModel(userPreferences)

    val isLoading by homeViewModel.isLoading.collectAsState()
    val userName by homeViewModel.userName.collectAsState()

    if (isLoading) {
        LoadingScreen() // Tampilkan loading screen jika sedang loading
    } else {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)  // Already includes padding, but no conflict with Scaffold's padding
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopHome(viewModel = homeViewModel)
            CameraSection(navHostController)
            AlarmSection()
            ArticleSection(navHostController)
        }
    }
}


@Composable
fun TopHome(viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    val userName by viewModel.userName.collectAsState()

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
                text = "Halo $userName 👋",
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
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Memuat data...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
@Composable
fun CameraSection(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
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
            colorText = MaterialTheme.colorScheme.onTertiaryContainer,
            onClick = {
                // Buka CameraPotoTanamActivity
                navController.navigate(Screen.CameraPotoTanam.route)
            }
        )
        CardCamera(
            modifier = Modifier.weight(0.5f),
            icon = R.drawable.flip,
            imageRes = R.drawable.scan_tanam,
            title = stringResource(R.string.txt_scantanam),
            description = stringResource(R.string.txt_dummy_scantanam),
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            borderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            colorText = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = {
                // Navigasi ke halaman hasil (Result) setelah mengklik card kedua
                navController.navigate(Screen.CameraScanTanam.route)
            }
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
                    navController.navigate(Screen.Article.route)
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
                        navController.navigate(Screen.DetailArticle.createRoute(article.id)) {
                            popUpTo(Screen.Home.route) { // Bersihkan halaman Home dari stack
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
    HidroQuTheme{
        val navController = rememberNavController()
        HomeActivity(navController)
    }
}
