package com.capstone.hidroqu.ui.screen.register

import android.annotation.SuppressLint
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.ui.component.TextFieldForm
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel()
) {
    var nameValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var passwordConfirmationValue by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordConfirmationError by remember { mutableStateOf<String?>(null) }
    var passwordLengthError by remember { mutableStateOf<String?>(null) }
    var showPassword by remember { mutableStateOf(false) } // To toggle visibility for password
    var showPasswordConfirmation by remember { mutableStateOf(false) } // To toggle visibility for confirmation password
    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var isPasswordConfirmationTouched by remember { mutableStateOf(false) }
    var isPasswordTouched by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val isLoading by viewModel.isLoading

    // Fungsi validasi terpisah
    fun validateForm(): Boolean {
        nameError = if (nameValue.isBlank()) "Nama tidak boleh kosong" else null
        emailError = if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) "Format email tidak valid" else null
        passwordError = when {
            passwordValue.isBlank() -> "Password tidak boleh kosong"
            passwordValue.length < 8 -> "Password minimal 8 karakter"
            else -> null
        }
        passwordConfirmationError = when {
            passwordConfirmationValue.isBlank() -> "Konfirmasi password tidak boleh kosong"
            passwordConfirmationValue.length < 8 -> "Konfirmasi password minimal 8 karakter" // Tambahkan validasi panjang
            passwordConfirmationValue != passwordValue -> "Konfirmasi password tidak sesuai"
            else -> null
        }
        return nameError == null && emailError == null && passwordError == null && passwordConfirmationError == null
    }

    LaunchedEffect(passwordValue) {
        if (isPasswordTouched) {
            passwordLengthError = if (passwordValue.length < 8) {
                "Password minimal 8 karakter"
            } else null
        }
    }

    LaunchedEffect(passwordConfirmationValue) {
        if (isPasswordConfirmationTouched) {
            passwordConfirmationError = when {
                passwordConfirmationValue.length < 8 -> "Konfirmasi password minimal 8 karakter"
                passwordConfirmationValue != passwordValue -> "Konfirmasi password tidak sesuai"
                else -> null
            }
        }
    }

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(title = "Daftar", navHostController = navHostController)
        },
        modifier = modifier
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
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
                    // Show loading spinner when isLoading is true
                    RegisterForm(
                        name = nameValue,
                        email = emailValue,
                        password = passwordValue,
                        passwordConfirmation = passwordConfirmationValue,
                        onNameChanged = {
                            nameValue = it
                            nameError = null
                        },
                        onEmailChanged = {
                            emailValue = it
                            emailError = null
                        },
                        onPasswordChanged = {
                            passwordValue = it
                            passwordError = null
                            isPasswordTouched = true // Mark the password field as touched
                        },
                        onPasswordConfirmationChanged = {
                            passwordConfirmationValue = it
                            passwordConfirmationError = null
                            isPasswordConfirmationTouched = true
                        },
                        nameError = nameError,
                        emailError = emailError,
                        passwordError = passwordError,
                        passwordConfirmationError = passwordConfirmationError,
                        passwordLengthError = passwordLengthError, // Pass the password length error
                        showPassword = showPassword, // Pass the showPassword state
                        showPasswordConfirmation = showPasswordConfirmation, // Pass the showPasswordConfirmation state
                        onShowPasswordChanged = { showPassword = it },
                        onShowPasswordConfirmationChanged = { showPasswordConfirmation = it }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    RegisterButton(
                        navController = navHostController,
                        onRegister = {
                            if (validateForm()) {
                                viewModel.registerUser(
                                    nameValue,
                                    emailValue,
                                    passwordValue,
                                    passwordConfirmationValue,
                                    onSuccess = {
                                        navHostController.navigate(Screen.Login.route) {
                                            popUpTo(Screen.Register.route) { inclusive = true }
                                        }
                                        isSuccess = true
                                        Toast.makeText(
                                            context,
                                            "Daftar berhasil, lets go:>",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onError = {
                                        isSuccess = false
                                        Toast.makeText(
                                            context,
                                            "Registrasi Anda gagal. Coba lagi, ya;-;",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LoginRedirectButton(navController = navHostController)
                }
            }
        }
    }
}


@Composable
fun RegisterForm(
    name: String,
    email: String,
    password: String,
    passwordConfirmation: String,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordConfirmationChanged: (String) -> Unit,
    nameError: String?,
    emailError: String?,
    passwordError: String?,
    passwordConfirmationError: String?,
    passwordLengthError: String?, // Add password length error
    showPassword: Boolean, // Add showPassword state
    showPasswordConfirmation: Boolean, // Add showPasswordConfirmation state
    onShowPasswordChanged: (Boolean) -> Unit, // Add onShowPasswordChanged callback
    onShowPasswordConfirmationChanged: (Boolean) -> Unit // Add onShowPasswordConfirmationChanged callback
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
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = passwordError != null || passwordLengthError != null,
        errorMessage = passwordLengthError
            ?: passwordError, // Show password length error if available
        trailingIcon = {
            IconButton(onClick = { onShowPasswordChanged(!showPassword) }) {
                Icon(
                    painter = painterResource(id = if (showPassword) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                    contentDescription = if (showPassword) "Sembunyikan Kata Sandi" else "Tampilkan Kata Sandi"
                )
            }
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = passwordConfirmation,
        onValueChange = onPasswordConfirmationChanged,
        label = "Konfirmasi Password",
        visualTransformation = if (showPasswordConfirmation) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = passwordConfirmationError != null,
        errorMessage = passwordConfirmationError,
        trailingIcon = {
            IconButton(onClick = { onShowPasswordConfirmationChanged(!showPasswordConfirmation) }) {
                Icon(
                    painter = painterResource(id = if (showPasswordConfirmation) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                    contentDescription = if (showPasswordConfirmation) "Sembunyikan Kata Sandi" else "Tampilkan Kata Sandi"
                )
            }
        }
    )
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
//        RegisterForm(
//            name = "",
//            email = "",
//            password = "",
//            passwordConfirmation = "",
//            onNameChanged = {},
//            onEmailChanged = {},
//            onPasswordChanged = {},
//            onPasswordConfirmationChanged = {},
//            nameError = null,
//            emailError = null,
//            passwordError = null,
//            passwordConfirmationError = null
//        )
    }
}
