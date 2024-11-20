package com.capstone.hidroqu.ui.screen.register

import android.annotation.SuppressLint
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.ui.component.TextFieldForm
import com.capstone.hidroqu.ui.theme.HidroQuTheme



@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun RegisterActivity(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    var nameValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Fungsi validasi terpisah
    fun validateForm(): Boolean {
        nameError = if (nameValue.isBlank()) "Nama tidak boleh kosong" else null
        emailError = if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) "Format email tidak valid" else null
        passwordError = if (passwordValue.isBlank()) "Password tidak boleh kosong" else null
        return nameError == null && emailError == null && passwordError == null
    }

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(title = "Daftar", navHostController = navHostController)
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                RegisterForm(
                    name = nameValue,
                    email = emailValue,
                    password = passwordValue,
                    onNameChanged = {
                        nameValue = it
                        nameError = null // Reset error saat teks berubah
                    },
                    onEmailChanged = {
                        emailValue = it
                        emailError = null // Reset error saat teks berubah
                    },
                    onPasswordChanged = {
                        passwordValue = it
                        passwordError = null // Reset error saat teks berubah
                    },
                    nameError = nameError,
                    emailError = emailError,
                    passwordError = passwordError
                )
                Spacer(modifier = Modifier.height(32.dp))

                RegisterButton(
                    navController = navHostController,
                    onRegister = {
                        if (validateForm()) {
                            navHostController.navigate(Screen.Login.route)
                        }
                    }
                )

                ForgotPasswordButton(navHostController = navHostController)
                Spacer(modifier = Modifier.height(8.dp))

                LoginRedirectButton(navController = navHostController)
            }
        }
    }
}

@Composable
fun RegisterForm(
    name: String,
    email: String,
    password: String,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    nameError: String?,
    emailError: String?,
    passwordError: String?
) {
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = onNameChanged,
        label = "Nama",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        isError = nameError != null,
        errorMessage = nameError
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = onEmailChanged,
        label = "Email",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = emailError != null,
        errorMessage = emailError
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChange = onPasswordChanged,
        label = "Password",
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = passwordError != null,
        errorMessage = passwordError
    )
}

@Composable
fun ForgotPasswordButton(
    navHostController: NavHostController
) {
    TextButton(
        onClick = { navHostController.navigate(Screen.ForgotPassword.route) }
    ) {
        Text(
            text = "Lupa password",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun RegisterButton(
    navController: NavHostController,
    onRegister: () -> Unit
) {
    Button(
        onClick = onRegister,
        shape = RoundedCornerShape(100.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    ) {
        Text(
            text = "Daftar",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun LoginRedirectButton(
    navController: NavHostController
) {
    TextButton(
        onClick = { navController.navigate(Screen.Login.route) }
    ) {
        Text(
            text = "Sudah punya akun? Masuk.",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterActivityPreview() {
    HidroQuTheme {
        RegisterForm(
            name = "",
            email = "",
            password = "",
            onNameChanged = {},
            onEmailChanged = {},
            onPasswordChanged = {},
            nameError = null,
            emailError = null,
            passwordError = null
        )
    }
}
