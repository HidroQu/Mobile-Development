package com.capstone.hidroqu.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.capstone.hidroqu.R

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigationItems = listOf(
        NavigationItem(
            title = "Home",
            icon = R.drawable.home,
            iconFilled = R.drawable.home_active,
            screen = Screen.Home
        ),
        NavigationItem(
            title = "Tanamanku",
            icon = R.drawable.tanamanku,
            iconFilled = R.drawable.tanamanku_active,
            screen = Screen.MyPlant
        ),
        NavigationItem(
            title = "Komunitas",
            icon = R.drawable.komunitas,
            iconFilled = R.drawable.komunitas_active,
            screen = Screen.Community
        ),
        NavigationItem(
            title = "Profil",
            icon = R.drawable.profil,
            iconFilled = R.drawable.profile_active,
            screen = Screen.Profile
        )
    )

    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary
    ) {
        navigationItems.map {
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (currentRoute == it.screen.route) it.iconFilled else it.icon
                        ),
                        contentDescription = it.title
                    )
                },
                label = {
                    Text(
                        it.title,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (currentRoute == it.screen.route) FontWeight.Bold else FontWeight.Normal,
                        )
                    )
                },
                selected = currentRoute == it.screen.route,
                onClick = {
                    navHostController.navigate(it.screen.route) {
                        popUpTo(Screen.Home.route){
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}
