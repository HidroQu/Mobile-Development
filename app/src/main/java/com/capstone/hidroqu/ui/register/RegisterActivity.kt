package com.capstone.hidroqu.ui.register

import android.util.Patterns
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.component.TextFieldForm
import com.capstone.hidroqu.ui.theme.HidroQuTheme

/**
 * Validasi format email menggunakan Android Patterns
 */
fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

/**
 * Komponen utama layar registrasi
 */
@Composable
fun RegisterActivity(
    name: String,
    email: String,
    password: String,
    checkValid: MutableList<Boolean>,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            RegisterTopBar(
                title = "Daftar",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        RegisterForm(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .pointerInput(Unit) {
                    detectTapGestures {
                        keyboardController?.hide()
                    }
                },
            name = name,
            email = email,
            password = password,
            onNameChanged = onNameChanged,
            onEmailChanged = onEmailChanged,
            onPasswordChanged = onPasswordChanged,
            onRegisterClicked = onRegisterClicked,
            onLoginClicked = onLoginClicked
        )
    }
}

/**
 * Top bar dengan tombol kembali
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(
                text = title,
                modifier = Modifier,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

/**
 * Form input registrasi
 */
@Composable
private fun RegisterForm(
    name: String,
    email: String,
    password: String,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRegisterClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nameValue by remember { mutableStateOf(name) }
    var emailValue by remember { mutableStateOf(email) }
    var passwordValue by remember { mutableStateOf(password) }

    var isNameValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    val isFormValid = isNameValid && isEmailValid && isPasswordValid

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Bagian Input Fields
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Input Nama
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

            // Input Email
            TextFieldForm(
                modifier = Modifier.fillMaxWidth(),
                value = emailValue,
                onValueChange = {
                    emailValue = it
                    onEmailChanged(it)
                    isEmailValid = it.isNotBlank() && isEmailValid(it)
                },
                label = "Email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = !isEmailValid,
                errorMessage = if (!isEmailValid) "Format email tidak valid" else null
            )

            // Input Password
            TextFieldForm(
                modifier = Modifier.fillMaxWidth(),
                value = passwordValue,
                onValueChange = {
                    passwordValue = it
                    onPasswordChanged(it)
                    isPasswordValid = it.isNotBlank()
                },
                label = "Password",
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !isPasswordValid,
                errorMessage = if (!isPasswordValid) "Password tidak boleh kosong" else null
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bagian Tombol
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ){
        Button(
            onClick = {
                isNameValid = nameValue.isNotBlank()
                isEmailValid = emailValue.isNotBlank() && isEmailValid(emailValue)
                isPasswordValid = passwordValue.isNotBlank()
                if (isFormValid) {
                    onRegisterClicked()
                }
            },
            enabled = isFormValid,
            shape = RoundedCornerShape(100.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Text(
                text = "Daftar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        TextButton(onClick = onLoginClicked) {
            Text(
                text = "Sudah punya akun? Masuk.",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.primary,
                ),
            )
        }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    HidroQuTheme {
        RegisterActivity(
            name = "",
            email = "",
            password = "",
            checkValid = mutableListOf(),
            onNameChanged = {},
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            onRegisterClicked = {},
            onBackClick = {}
        )
    }
}