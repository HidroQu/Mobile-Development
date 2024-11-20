package com.capstone.hidroqu.ui.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.TopBarDefault
import com.capstone.hidroqu.utils.ListUserData
import com.capstone.hidroqu.utils.dummyListUserData
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun ProfileActivity(
    navHostController: NavHostController,
    userData: ListUserData,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            // You can customize your top bar here, if needed
            TopBarDefault("Profil anda")
        },
        content = { paddingValues ->
            // Profile Content
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
                            painter = painterResource(id = userData.img), // Use the dynamic image
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
                    AppearanceSettings()
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
fun AppearanceSettings() {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            "Appearance",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground // Appearance label color set to onBackground
        )
        AppearanceOption("System default", selected = true)
        AppearanceOption("Light", selected = false)
        AppearanceOption("Dark", selected = false)
    }
}

@Composable
fun AppearanceOption(text: String, selected: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        RadioButton(
            selected = selected,
            onClick = { /* Handle selection */ },
            colors = RadioButtonDefaults.colors(
                unselectedColor = MaterialTheme.colorScheme.outline, // Set radio button border color to outline
                selectedColor = MaterialTheme.colorScheme.primary // Set selected border color to primary
            )
        )
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    HidroQuTheme {
        val navController = rememberNavController()
        val userData = dummyListUserData.first() // Ambil data pertama dari dummy list
        ProfileActivity(navController, userData)
    }
}
