package com.capstone.hidroqu

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.ui.comunity.ComunityActivity
import com.capstone.hidroqu.ui.home.HomeActivity
import com.capstone.hidroqu.ui.myplant.MyPlantActivity
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.capstone.hidroqu.ui.profile.ProfileActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            true
        }

        Handler(Looper.getMainLooper()).postDelayed({
            splashScreen.setKeepOnScreenCondition {
                false
            }
        }, 2000)
        enableEdgeToEdge()
        setContent {
            HidroQuTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
//        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        // Content area
        NavHost(
            navController = navController,
            startDestination = "Home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Home") { HomeActivity() }
            composable("Tanamanku") { MyPlantActivity() }
            composable("Komunitas") { ComunityActivity() }
            composable("Profil") { ProfileActivity() } }
        }
    }

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Tanamanku", "Komunitas", "Profil")
    val routes = listOf("Home", "Tanamanku", "Komunitas", "Profil")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.Person, Icons.Filled.Person)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.FavoriteBorder, Icons.Outlined.Person, Icons.Outlined.Person)
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary, // Warna latar belakang
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = {
                    Text(
                        item,
                        style = MaterialTheme.typography.labelLarge.copy( // Pilih typografi yang sesuai
                            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight = if (selectedItem == index) FontWeight.Bold else FontWeight.Normal, // Bold jika dipilih
                        )
                    )
                },
                selected = selectedItem == index,
                onClick =
                {
                    selectedItem = index
                    navController.navigate(routes[index]) {
                        // Hindari membuat rute berulang di back stack
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer, // Warna ikon saat dipilih
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant, // Warna ikon saat tidak dipilih
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer, // Warna teks saat dipilih
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant, // Warna teks saat tidak dipilih
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

@Preview
@Composable
private fun MainAppPreview() {
    HidroQuTheme {
        MainApp()
    }
}

