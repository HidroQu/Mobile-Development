package com.capstone.hidroqu.ui.screen.forgotpassword

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.navigation.SimpleLightTopAppBar
import com.capstone.hidroqu.ui.component.TextFieldForm
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.hidroqu.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ForgotPasswordScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel()
) {
    var emailValue by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading
    val context = LocalContext.current

    fun validateForm(): Boolean {
        emailError = if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) "Format email tidak valid" else null
        return emailError == null
    }

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(title = "Lupa Kata Sandi", navHostController = navHostController)
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
        }else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    ForgotPasswordForm(
                        email = emailValue,
                        onEmailChanged = {
                            emailValue = it
                            emailError = null
                        },
                        emailError = emailError,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ForgotPasswordButton(
                        navController = navHostController,
                        onForget = {
                            if (validateForm()) {
                                viewModel.forgotPassword(
                                    emailValue,
                                    onSuccess = {
                                        Toast.makeText(context, "Tautan berhasil dikirim ke email Anda!", Toast.LENGTH_SHORT).show()
                                        isSuccess = true
                                    },
                                    onError = {
                                        Toast.makeText(context, "Terjadi kesalahan. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                                        isSuccess = false
                                    }
                                )
                            }
                        }
                    )
                    LoginRedirectButton(navController = navHostController)
                }
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(50.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordForm(
    email: String,
    onEmailChanged: (String) -> Unit,
    emailError: String?,
) {
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = onEmailChanged,
        label = "Email",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = emailError != null,
        errorMessage = emailError
    )
}

@Composable
fun ForgotPasswordButton(
    navController: NavHostController,
    onForget: () -> Unit
) {
    Button(
        onClick = onForget,
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
            text = "Kirim Tautan Pemulihan",
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
        onClick = { navController.navigate(Screen.Login.route)}
    ) {
        Text(
            text = "Kembali ke halaman masuk..",
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
fun ForgetPasswordActivityPreview() {
    HidroQuTheme {
        ForgotPasswordScreen(
            navHostController = NavHostController(context = LocalContext.current),
        )
    }
}