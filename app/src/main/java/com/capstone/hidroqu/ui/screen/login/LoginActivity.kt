package com.capstone.hidroqu.ui.screen.login

import android.annotation.SuppressLint
import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.ui.component.TextFieldForm
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.hidroqu.ui.viewmodel.AuthViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginActivity(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel()
) {
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf("") }

    // Fungsi validasi
    fun validateForm(): Boolean {
        emailError = if (!Patterns.EMAIL_ADDRESS.matcher(emailValue)
                .matches()
        ) "Format email tidak valid" else null
        passwordError = if (passwordValue.isBlank()) "Password tidak boleh kosong" else null
        return emailError == null && passwordError == null
    }
    val isLoading by viewModel.isLoading
    val context = LocalContext.current // <-- Use it here inside @Composable function
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.hidroponikbg),
            contentDescription = "Hidroponik Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { keyboardController?.hide() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Title
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Masuk dulu, yuk!",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Login Form
                    LoginForm(  // Pass emailValue and passwordValue here
                        email = emailValue,
                        password = passwordValue,
                        onEmailChanged = {
                            emailValue = it
                            emailError = null // Reset error saat input berubah
                        },
                        onPasswordChanged = {
                            passwordValue = it
                            passwordError = null // Reset error saat input berubah
                        },
                        emailError = emailError,
                        passwordError = passwordError
                    )
                    ForgotPasswordButton(navHostController = navHostController)
                    Spacer(modifier = Modifier.height(32.dp))

                    // Login Button
                    LoginButton(
                        navHostController = navHostController,
                        onLogin = {
                            if (validateForm()) {
                                viewModel.loginUser(
                                    emailValue,
                                    passwordValue,
                                    context = context,
                                    onSuccess = { loginResponse ->
                                        navHostController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Login.route) { inclusive = true }
                                        }
                                        message = "Login Successful! Token: ${loginResponse.data.token}"
                                    },
                                    onError = { error ->
                                        message = error
                                    }
                                )
                            }
                        }
                    )
                    Text(message)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Google Login Button
                    GoogleButton(navHostController = navHostController)

                    // Register Button
                    RegisterButton(navHostController = navHostController)

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}


@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    emailError: String?,
    passwordError: String?
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
    Spacer(modifier = Modifier.height(8.dp))
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChange = onPasswordChanged,
        label = "Kata Sandi",
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = passwordError != null,
        errorMessage = passwordError
    )
}


@Composable
fun LoginButton(
    navHostController: NavHostController,
    onLogin: () -> Unit
) {
    Button(
        onClick = onLogin,
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
            text = "Masuk",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
fun RegisterButton(
    navHostController: NavHostController
) {
    TextButton(
        onClick = { navHostController.navigate(Screen.Register.route) }
    ) {
        Text(
            text = "Belum punya akun? Daftar.",
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
fun ForgotPasswordButton(
    navHostController: NavHostController
) {

    Text(
        text = "Lupa password",
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navHostController.navigate(Screen.ForgotPassword.route)
            }
            .padding(vertical = 4.dp, horizontal = 2.dp),
        textAlign = TextAlign.End,
    )
}


@Composable
fun GoogleButton(
    navHostController: NavHostController,
) {
    TextButton(
        onClick = {
            navHostController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(100.dp)
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google Logo",
                modifier = Modifier.size(16.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Masuk dengan Google",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}
