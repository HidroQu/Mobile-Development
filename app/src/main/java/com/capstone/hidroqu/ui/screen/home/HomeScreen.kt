package com.capstone.hidroqu.ui.screen.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.component.CardAlarm
import com.capstone.hidroqu.ui.component.CardArticle
import com.capstone.hidroqu.ui.component.CardCamera
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.nonui.data.ArticleDetailResponse
import com.capstone.hidroqu.nonui.data.MyPlantResponse
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.viewmodel.ArticleViewModel
import com.capstone.hidroqu.ui.viewmodel.HomeViewModel
import com.capstone.hidroqu.ui.viewmodel.MyPlantViewModel


@Composable
fun HomeScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    myPlantViewModel: MyPlantViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    val userPreferences = UserPreferences(context)
    val articleViewModel = viewModel<ArticleViewModel>()
    val viewModel = HomeViewModel(userPreferences)
    val isLoading by viewModel.isLoading.collectAsState()

    val token by userPreferences.token.collectAsState(initial = null)

    val articles by articleViewModel.articles.collectAsState(emptyList())
    val plantsToHarvest by myPlantViewModel.myPlants.collectAsState(emptyList())

    LaunchedEffect(token) {
        if (token == null) {
            navHostController.navigate(Screen.Login.route) {
                popUpTo(Screen.HomeRoute.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(token) {
        token?.let {
            articleViewModel.fetchAllArticles(it)
            myPlantViewModel.fetchMyPlants(it)
        } ?: run {
            navHostController.navigate(Screen.DetailArticle.route)
        }
    }

    if (isLoading) {
        LoadingScreen()
    } else {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopHome(viewModel = viewModel)
            CameraSection(navHostController)
            AlarmSection(
                userPreferences = userPreferences,
                plantsToHarvest = plantsToHarvest,
                onDetailClicked = { plantId ->
                    navHostController.navigate(Screen.DetailMyPlant.createRoute(plantId))
                })
            ArticleSection(navHostController, articles = articles)
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
            .padding(0.dp),
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
                navController.navigate(Screen.PotoTanamRoute.route)
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
                navController.navigate(Screen.ScanTanamRoute.route)
                {
                    popUpTo(Screen.Home.route)
                    { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

@Composable
fun NoPlantList(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.panen),
                contentDescription = "Artikel",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Jangan Lewatkan Waktu Panen, Setel Alarm Panenmu!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun AlarmSection(
    userPreferences: UserPreferences,
    plantsToHarvest: List<MyPlantResponse>,
    modifier: Modifier = Modifier,
    onDetailClicked: (Int) -> Unit
) {
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Log.d("AlarmSection", "Plants to harvest: $plantsToHarvest")

            val plantsWithNotification = plantsToHarvest.filter { plant ->
                val isNotificationEnabled by userPreferences.getPlantNotificationEnabled(plant.id)
                    .collectAsState(initial = false)
                isNotificationEnabled
            }
            if (plantsWithNotification.isNotEmpty()) {
                plantsWithNotification.forEach { plant ->
                    CardAlarm(
                        listAlarmHome = plant,
                        onClick = {
                            onDetailClicked(plant.id)
                        }
                    )
                }
            } else {
                NoPlantList(modifier = modifier)
            }
        }
    }
}


@Composable
fun ArticleSection(navController: NavHostController, modifier: Modifier = Modifier, articles: List<ArticleDetailResponse>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
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
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.Article.route)
                    }
                    .wrapContentWidth(Alignment.End)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            articles.take(5).forEach { article ->
                CardArticle(
                    article = article,
                    onClick = {
                        navController.navigate(Screen.DetailArticle.createRoute(article.id)) {
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
        NoPlantList(modifier = Modifier)
    }
}