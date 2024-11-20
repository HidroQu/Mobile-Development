package com.capstone.hidroqu.ui.screen.resetpassword

import android.annotation.SuppressLint
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
fun ResetPasswordActivity(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    // Fungsi validasi terpisah
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
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
                            navHostController.navigate(Screen.Login.route)
                        }
                    },
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
                        text = "Atur Ulang Kata Sandi",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                LoginRedirectButton(navController = navHostController)
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
fun ResetPasswordActivityPreview() {
    HidroQuTheme {
        ResetPasswordForm(
            newPassword = "",
            confirmPassword = "",
            onNewPasswordChanged = {},
            onConfirmPasswordChanged = {},
            newPasswordError = null,
            newConfirmPasswordError = null
        )
    }
}
