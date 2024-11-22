package com.capstone.hidroqu.ui.screen.profile

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.TopBarDefault
import com.capstone.hidroqu.utils.ListUserData
import com.capstone.hidroqu.utils.dummyListUserData
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.ThemeViewModel

@Composable
fun ProfileActivity(
    navHostController: NavHostController,
    userData: ListUserData,
    themeViewModel: ThemeViewModel,
    modifier: Modifier = Modifier
) {
    val themeMode by themeViewModel.themeMode.collectAsState()

    Scaffold(
        topBar = {
            TopBarDefault("Profil anda")
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Profile Header
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    // Profile Picture
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = userData.img),
                            contentDescription = "Profile Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    // Profile Name and Description
                    ProfileInfo(
                        name = userData.name,
                        description = userData.bio
                    )

                    // Edit Profile Button (Outlined)
                    OutlinedButton(
                        onClick = { navHostController.navigate(Screen.EditProfile.route) },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Edit Profile",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    // Appearance Settings (Theme change)
                    AppearanceSettings(
                        selectedMode = themeMode,
                        onModeChange = { newMode -> themeViewModel.setTheme(newMode.lowercase()) }
                    )
                }
            }
        }
    )
}

@Composable
fun ProfileInfo(name: String, description: String) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun AppearanceSettings(
    selectedMode: String,
    onModeChange: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            "Appearance",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground // Appearance label color set to onBackground
        )
        AppearanceOption("System", selected = selectedMode == "system", onClick = { onModeChange("system") })
        AppearanceOption("Light", selected = selectedMode == "light", onClick = { onModeChange("light") })
        AppearanceOption("Dark", selected = selectedMode == "dark", onClick = { onModeChange("dark") })
    }
}

@Composable
fun AppearanceOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.outline
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    HidroQuTheme {
        val navController = rememberNavController()
        val userData = dummyListUserData.first()
    }
}
