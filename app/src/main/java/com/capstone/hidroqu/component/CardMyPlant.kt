package com.capstone.hidroqu.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.ui.myplant.ListMyPlantTanamanku

@Composable
fun CardMyPlant(
    ListMyPlantTanamanku: ListMyPlantTanamanku,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary, shape = MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = ListMyPlantTanamanku.imgMyPlantTanamanku),
            contentDescription = "Gambar Tanaman",
            modifier = Modifier.size(40.dp)
        )
        Column {
            Text(
                text = stringResource(id = ListMyPlantTanamanku.txtMyPlantTanamanku),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = stringResource(id = ListMyPlantTanamanku.descMyPlantTanamanku),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1
            )
        }
    }
}