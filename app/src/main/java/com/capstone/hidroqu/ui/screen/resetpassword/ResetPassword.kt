package com.capstone.hidroqu.ui.screen.resetpassword

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.ui.component.TextFieldForm
import com.capstone.hidroqu.ui.screen.forgetpassword.ForgotPasswordActivity
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.ui.viewmodel.AuthViewModel

import android.widget.Toast
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ResetPasswordActivity(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val intent = (context as? ComponentActivity)?.intent
    val uri: Uri? = intent?.data

    // Capture token and email from URI
    var token by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var serverResponse by remember { mutableStateOf<String?>(null) }

    // Get loading state from ViewModel
    val isLoading by viewModel.isLoading

    LaunchedEffect(uri) {
        if (uri != null) {
            token = uri.getQueryParameter("token") ?: ""
            email = uri.getQueryParameter("email") ?: ""
        }
    }

    LaunchedEffect(newPassword) {
        newPasswordError = if (newPassword.length < 8) {
            "Password minimal 8 karakter"
        } else null
    }

    fun validateForm(): Boolean {
        confirmPasswordError = if (confirmPassword != newPassword) "Kata sandi tidak cocok" else null
        return newPasswordError == null && confirmPasswordError == null
    }

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(title = "Pemulihan Kata Sandi", navHostController = navHostController)
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
                Text("Email: $email")
                Text("Token: $token")

                ResetPasswordForm(
                    newPassword = newPassword,
                    confirmPassword = confirmPassword,
                    onNewPasswordChanged = {
                        newPassword = it
                    },
                    onConfirmPasswordChanged = {
                        confirmPassword = it
                        confirmPasswordError = null // Reset error when text changes
                    },
                    newPasswordError = newPasswordError,
                    newConfirmPasswordError = confirmPasswordError
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (validateForm()) {
                            viewModel.resetPassword(
                                token = token,
                                email = email,
                                password = newPassword,
                                onSuccess = {
                                    serverResponse = it
                                    Toast.makeText(context, "Kata sandi berhasil diatur ulang.", Toast.LENGTH_SHORT).show()
                                    navHostController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.ResetPassword.route) { inclusive = true }
                                    }
                                },
                                onError = {
                                    serverResponse = it
                                    Toast.makeText(context, "Gagal mengatur ulang kata sandi.", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    },
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Atur Ulang Kata Sandi",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                serverResponse?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ResetPasswordForm(
    newPassword: String,
    confirmPassword: String,
    onNewPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    newPasswordError: String?,
    newConfirmPasswordError: String?,
) {
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = newPassword,
        onValueChange = onNewPasswordChanged,
        label = "Kata Sandi Baru",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = newPassword.isNotEmpty() && newPassword.length < 8,
        errorMessage = if (newPassword.isNotEmpty() && newPassword.length < 8) "Password minimal 8 karakter" else null,
        visualTransformation = PasswordVisualTransformation()
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = confirmPassword,
        onValueChange = onConfirmPasswordChanged,
        label = "Konfirmasi Kata Sandi",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = confirmPassword.isNotEmpty() && (confirmPassword != newPassword || confirmPassword.length < 8),
        errorMessage = when {
            confirmPassword.isNotEmpty() && confirmPassword.length < 8 -> "Password minimal 8 karakter"
            confirmPassword.isNotEmpty() && confirmPassword != newPassword -> "Kata sandi tidak cocok"
            else -> null
        },
        visualTransformation = PasswordVisualTransformation()
    )
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordActivityPreview() {
    HidroQuTheme {
        ResetPasswordActivity(
            navHostController = NavHostController(context = LocalContext.current),
        )
    }
}
