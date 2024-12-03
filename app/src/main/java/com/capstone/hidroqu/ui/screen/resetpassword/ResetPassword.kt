package com.capstone.hidroqu.ui.screen.resetpassword

import android.annotation.SuppressLint
import android.net.Uri
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

    // Menangkap token dan email dari URI
    var token by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(uri) {
        if (uri != null) {
            token = uri.getQueryParameter("token") ?: ""
            email = uri.getQueryParameter("email") ?: ""
        }
    }

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var serverResponse by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf("") }

    fun validateForm(): Boolean {
        newPasswordError = if (newPassword.isBlank()) "Kata sandi baru tidak boleh kosong" else null
        confirmPasswordError = if (confirmPassword != newPassword) "Kata sandi tidak cocok" else null
        return newPasswordError == null && confirmPasswordError == null
    }

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(title = "Pemulihan Kata Sandi", navHostController = navHostController)
        },
        modifier = modifier
    ) { paddingValues ->
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
                    newPasswordError = null
                },
                onConfirmPasswordChanged = {
                    confirmPassword = it
                    confirmPasswordError = null
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
                                // Tampilkan Toast untuk sukses
                                Toast.makeText(context, "Kata sandi berhasil diatur ulang.", Toast.LENGTH_SHORT).show()
                                navHostController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.ResetPassword.route) { inclusive = true }
                                }
                            },
                            onError = {
                                serverResponse = it
                                // Tampilkan Toast untuk error
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
        isError = newPasswordError != null,
        errorMessage = newPasswordError,
        visualTransformation = PasswordVisualTransformation()
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = confirmPassword,
        onValueChange = onConfirmPasswordChanged,
        label = "Konfirmasi Kata Sandi",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = newConfirmPasswordError != null,
        errorMessage = newConfirmPasswordError,
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
