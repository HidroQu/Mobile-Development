package com.capstone.hidroqu.ui.screen.editprofile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.component.TextFieldForm
import com.capstone.hidroqu.utils.ListUserData
import com.capstone.hidroqu.utils.dummyListUserData
import com.capstone.hidroqu.ui.theme.HidroQuTheme

@Composable
fun EditProfileActivity(
    userData: ListUserData,  // Menambahkan parameter userData
    onNameChanged: (String) -> Unit,
    onBioChanged: (String) -> Unit
) {
    var nameValue by remember { mutableStateOf(userData.name) }  // Menggunakan nama dari userData
    var bioValue by remember { mutableStateOf(userData.bio) }    // Menggunakan bio dari userData
    var profileImage by remember { mutableStateOf<Bitmap?>(null) }

    var isNameValid by remember { mutableStateOf(true) }
    var isBioValid by remember { mutableStateOf(true) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                profileImage = imageBitmap
            }
        }
    }

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
                    .clickable {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        launcher.launch(intent)
                    }
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                profileImage?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(120.dp)
                    )
                } ?: Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Default Profile Icon",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        // TextField for Name
        TextFieldForm(
            modifier = Modifier.fillMaxWidth(),
            value = nameValue,
            onValueChange = {
                nameValue = it
                onNameChanged(it)
                isNameValid = it.isNotBlank()
            },
            label = "Nama Lengkap",
            isError = !isNameValid,
            errorMessage = if (!isNameValid) "Nama tidak boleh kosong" else null
        )

        // TextField for Bio
        TextFieldForm(
            modifier = Modifier.fillMaxWidth(),
            value = bioValue,
            onValueChange = {
                bioValue = it
                onBioChanged(it)
                isBioValid = it.isNotBlank()
            },
            label = "Bio",
            isError = !isBioValid,
            errorMessage = if (!isBioValid) "Bio Tidak boleh kosong" else null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditProfileScreen() {
    HidroQuTheme {
        EditProfileActivity(
            userData = dummyListUserData.first(),  // Menggunakan data pertama dari dummyListUserData
            onNameChanged = {},
            onBioChanged = {}
        )
    }
}

