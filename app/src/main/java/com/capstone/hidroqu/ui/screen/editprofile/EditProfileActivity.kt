package com.capstone.hidroqu.ui.screen.editprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.TopBarAction
import com.capstone.hidroqu.nonui.data.UserPreferences
import com.capstone.hidroqu.ui.component.TextFieldForm
import com.capstone.hidroqu.ui.screen.myplant.NoPlantList
import com.capstone.hidroqu.utils.ListUserData
import com.capstone.hidroqu.utils.dummyListUserData
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun EditProfileActivity(
    navHostController: NavHostController,
    profileViewModel: ProfileViewModel = viewModel(),
    context: Context = LocalContext.current,
) {
    val userPreferences = UserPreferences(context)
    val token by userPreferences.token.collectAsState(initial = null)
    val userData by profileViewModel.userData.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val errorMessage by profileViewModel.errorMessage.collectAsState()

    var nameValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var bioValue by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

// Update nilai default saat userData berubah
    LaunchedEffect(userData) {
        userData?.let {
            nameValue = it.name ?: ""
            emailValue = it.email ?: ""
            bioValue = it.bio ?: ""
            // Jika ada URL gambar profil, inisialisasi Uri
            if (!it.photo.isNullOrEmpty()) {
                profileImageUri = Uri.parse(it.photo)
            }
        }
    }

    LaunchedEffect(token) {
        token?.let {
            profileViewModel.fetchUserProfile(it)
        } ?: run {
            navHostController.navigate(Screen.AuthRoute.route) {
                popUpTo(Screen.ProfileRoute.route) { inclusive = true }
            }
        }
    }

    // State for editable fields
    var passwordValue by remember { mutableStateOf("") }
    var confirmPasswordValue by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri // Simpan URI yang dipilih
    }

    val isPasswordValid = passwordValue.isEmpty() || (passwordValue == confirmPasswordValue)
    val isActionEnabled = nameValue.isNotBlank() &&
            emailValue.isNotBlank() &&
            android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() &&
            isPasswordValid



    Scaffold(
        topBar = {
            // TopBar can be customized if needed
            TopBarAction(
                title = "Edit Profil",
                navHostController = navHostController,
                onActionClick = {
                    if (!isPasswordValid) {
                        Toast.makeText(context, "Kata sandi dan konfirmasi kata sandi tidak cocok", Toast.LENGTH_SHORT).show()
                    } else {
                        token?.let {
                            Log.d("ProfileUpdate", "Sending data - Name: $nameValue, Email: $emailValue, Bio: $bioValue, Photo: $profileImageUri, password: $passwordValue, confirm: $passwordValue")

                            profileViewModel.updateProfile(
                                token = it,
                                name = nameValue,
                                email = emailValue,
                                bio = bioValue,
                                password = if (passwordValue.isNotEmpty()) passwordValue else null,
                                confirmPassword = if (passwordValue.isNotEmpty()) passwordValue else null,
                                photoUri = profileImageUri, // Kirim Uri langsung,
                                context = context,
                                onComplete = { success, message ->
                                    if (success) {
                                        Toast.makeText(context, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                                        navHostController.popBackStack() // Kembali ke halaman sebelumnya
                                    } else {
                                        Toast.makeText(context, "Gagal memperbarui profil: $message", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                },
                isActionEnabled = isActionEnabled,
                actionIcon = Icons.Default.Check
            )
        },
        content = { paddingValues ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (!errorMessage.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center // Atur konten ke tengah
                ) {
                    NoPlantList() // Panggil fungsi NoPlantList
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    EditForm(
                        name = nameValue,
                        bio = bioValue,
                        email = emailValue,
                        password = passwordValue,
                        confirmPassword = confirmPasswordValue,
                        profileImageUri = profileImageUri,
                        onProfileImageClicked = {
                            launcher.launch("image/*") // Filter hanya untuk gambar
                        },
                        onNameChanged = { nameValue = it },
                        onBioChanged = { bioValue = it },
                        onEmailChanged = { emailValue = it },
                        onPasswordChanged = { passwordValue = it },
                        onConfirmPasswordChanged = { confirmPasswordValue = it },
                        nameError = when {
                            nameValue.isBlank() -> "Nama tidak boleh kosong"
                            nameValue.length > 255 -> "Nama maksimal 255 karakter"
                            else -> null
                        },
                        bioError = when {
                            bioValue.isBlank() -> "Bio tidak boleh kosong"
                            bioValue.length > 255 -> "Bio maksimal 255 karakter"
                            else -> null
                        },
                        emailError = when {
                            emailValue.isBlank() -> "Email tidak boleh kosong"
                            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() -> "Format email tidak valid"
                            emailValue.length > 255 -> "Email maksimal 255 karakter"
                            else -> null
                        },
                        passwordError = when {
                            passwordValue.isNotEmpty() && passwordValue.length < 6 -> "Kata sandi minimal 6 karakter"
                            passwordValue.length > 255 -> "Kata sandi maksimal 255 karakter"
                            else -> null
                        },
                        confirmPasswordError = when {
                            confirmPasswordValue.isNotEmpty() && passwordValue != confirmPasswordValue ->
                                "Kata sandi dan konfirmasi kata sandi tidak cocok"
                            confirmPasswordValue.isNotEmpty() && confirmPasswordValue.length < 6 ->
                                "Kata sandi minimal 6 karakter"
                            confirmPasswordValue.length > 255 ->
                                "Kata sandi maksimal 255 karakter"
                            else -> null
                        }
                    )
                }
            }
        }
    )
}


@Composable
fun EditForm(
    name: String,
    bio: String,
    email: String,
    password: String,
    confirmPassword: String,
    profileImageUri: Uri?,
    onProfileImageClicked: () -> Unit,
    onNameChanged: (String) -> Unit,
    onBioChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,

    emailError: String?,
    passwordError: String?,
    confirmPasswordError: String?,
    nameError: String?,
    bioError: String?
){
// Profile Header
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // Profile Picture Section
        Box(
            modifier = Modifier
                .size(120.dp)
                .clickable { onProfileImageClicked() }
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            profileImageUri?.let {
                AsyncImage(
                    model = it, // Gunakan URI untuk memuat gambar
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                    error = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Crop
                )
            } ?: Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Default Profile Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(60.dp)
            )
        }

        // TextField for Name
        TextFieldForm(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = onNameChanged,
            label = "Nama lengkap",
            isError = nameError != null,
            errorMessage = nameError,
        )

        // TextField for Bio
        TextFieldForm(
            modifier = Modifier.fillMaxWidth(),
            value = bio,
            onValueChange = onBioChanged,
            label = "Bio",
            singleLine = false,  // Multiline untuk Bio
            maxLines = 5,
            isError = bioError != null,
            errorMessage = bioError,
        )
        TextFieldForm(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = onEmailChanged,
            label = "Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError != null,
            errorMessage = emailError
        )
        TextFieldForm(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = onPasswordChanged,
            label = "Ubah kata sandi",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError != null,
            errorMessage = passwordError
        )
        TextFieldForm(
            modifier = Modifier.fillMaxWidth(),
            value = confirmPassword,
            onValueChange = onConfirmPasswordChanged,
            label = "Konfirmasi kata sandi",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError != null,
            errorMessage = confirmPasswordError
        )
    }
}
