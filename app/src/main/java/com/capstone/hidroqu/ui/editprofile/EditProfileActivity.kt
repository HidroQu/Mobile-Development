package com.capstone.hidroqu.ui.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileActivity() {
    var username by remember { mutableStateOf(TextFieldValue("@User12312312")) }
    var name by remember { mutableStateOf(TextFieldValue("tifah")) }
    var bio by remember { mutableStateOf(TextFieldValue("Android Developer Advocate @google, sketch comedienne, opera singer. BLM.")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Person, // Menggunakan ikon Material3
                contentDescription = "Profile Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Username Field
        Text("Username", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Name Field
        Text("Nama anda", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Bio Field
        Text("Bio", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditProfileScreen() {
    HidroQuTheme {
        EditProfileActivity()
    }
}
