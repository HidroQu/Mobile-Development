package com.capstone.hidroqu.ui.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.ui.home.AlarmSection
import com.capstone.hidroqu.ui.home.ArticleSection
import com.capstone.hidroqu.ui.home.CameraSection
import com.capstone.hidroqu.ui.home.TopHome
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun ArticleActivity(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ArticleSection()
    }
}

@Preview(device = Devices.DEFAULT, showBackground = true)
@Composable
fun ArticleActivityPreview() {
    HidroQuTheme {
        ArticleActivity()
    }
}
