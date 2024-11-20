package com.capstone.hidroqu.ui.screen.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.TopBarDefault
import com.capstone.hidroqu.ui.component.CardCommunity
import com.capstone.hidroqu.utils.dummyListPlants
import com.capstone.hidroqu.utils.dummyListCommunity
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityActivity(
    navHostController: NavHostController,
    onAddClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopBarDefault("Komunitas")
        },
        floatingActionButton = {
            AskButton(onClick = onAddClicked)
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues) // Gunakan padding dari Scaffold
                .fillMaxSize()
        ) {
            if (dummyListCommunity.isEmpty()) {
                NoPostList()
            } else {
                PostList(onDetailClicked = { idPost ->
                    navHostController.navigate(Screen.DetailCommunity.createRoute(idPost)){
                        popUpTo(Screen.Community.route)
                    }
                })
            }
        }
    }
}

@Composable
fun AskButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Add Post")
            Text(
                text = "Tanya Komunitas",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun NoPostList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Belum ada postingan",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tambahkan postingan pertama anda",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PostList(modifier: Modifier = Modifier, onDetailClicked: (Int) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(20.dp)
    ) {
        dummyListCommunity.forEach { post ->
            CardCommunity (
                listCommunity = post,
                onClick = {
                    onDetailClicked(post.idPost)
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CommunityActivityPreview() {
    HidroQuTheme {
        val navController = rememberNavController()
        CommunityActivity(navController,
            onAddClicked = {
            },
        )
    }
}
