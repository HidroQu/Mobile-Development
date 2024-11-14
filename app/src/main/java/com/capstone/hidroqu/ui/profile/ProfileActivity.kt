package com.capstone.hidroqu.ui.profile

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
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun ProfileActivity(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile Icon",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(60.dp)
                )
            }

            // Profile Name and Description
            ProfileInfo(
                name = "Tiffah",
                description = "Android Developer Advocate @google, sketch comedienne, opera singer. BLM."
            )

            // Edit Profile Button (Outlined)
            OutlinedButton(
                onClick = {
                    navController.navigate("EditProfil") {
                        popUpTo("Profil") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary) // Sets border to primary color
            ) {
                Text(
                    text = "Edit Profile",
                    color = MaterialTheme.colorScheme.primary, // Text color set to primary color
                    style = MaterialTheme.typography.labelLarge
                )
            }

            // Appearance Settings
            AppearanceSettings()
        }
    }
}

@Composable
fun ProfileInfo(name: String, description: String) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface // Name text color set to onSurface
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface // Description text color set to onSurface
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
        ProfileActivity(navController)
    }
}
