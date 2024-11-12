package com.capstone.hidroqu.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.R

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HidroQuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeHidroQu()
                }
            }
        }
    }
}

@Composable
fun HomeHidroQu(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopHome()
        CameraSection()
        AlarmCardHome()
        ArticleSection()
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
        CameraCard(
            modifier = Modifier.weight(0.5f),
            icon = R.drawable.camera,
            imageRes = R.drawable.poto_tanam,
            title = stringResource(R.string.txt_pototanam),
            description = stringResource(R.string.txt_dummy_pototanam),
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            borderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
            colorText = MaterialTheme.colorScheme.onTertiaryContainer
        )
        CameraCard(
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
fun CameraCard(
    imageRes: Int,
    icon: Int,
    title: String,
    description: String,
    backgroundColor: Color,
    borderColor: Color,
    colorText: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(95.dp)
            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = borderColor, // Warna outline
                shape = MaterialTheme.shapes.medium // Bentuk sesuai Card
            )
            .padding(16.dp), // Padding internal kartu
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
//            modifier = Modifier.size(48.dp)
        )
        Column (
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    painterResource(icon),
                    contentDescription = "Camera",
                    modifier = Modifier.width(14.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 13.sp
                    ),
                    color = colorText
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 11.sp
                ),
                color = colorText
            )
        }
    }
}


@Composable
fun AlarmCardHome(modifier: Modifier = Modifier) {
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
fun CardAlarm(listAlarmHome: ListAlarmHome, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim, // Warna outline
                shape = MaterialTheme.shapes.medium // Bentuk sesuai Card
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = listAlarmHome.imgAlarmHome),
            contentDescription = "Gambar Tanaman",
            modifier = Modifier.size(40.dp)
        )
        Column {
            Text(
                text = stringResource(id = listAlarmHome.txtAlarmHome),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = stringResource(id = listAlarmHome.descAlarmHome),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ArticleSection(modifier: Modifier = Modifier) {
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
                color = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp) // Jarak antar artikel
        ) {
            dummyListArticles.forEach { article ->
                CardArticle(article)
            }
        }
    }
}

@Composable
fun CardArticle(article: ListArticleHome, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim, // Warna outline
                shape = MaterialTheme.shapes.medium // Bentuk sesuai Card
            ),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = MaterialTheme.shapes.medium
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Artikel",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Column (
            modifier = modifier
                .padding(16.dp)
        ){
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = article.summary,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview(device = Devices.DEFAULT, showBackground = true)
@Composable
fun HomeHidroQuPreview() {
    HidroQuTheme {
        HomeHidroQu()
    }
}
