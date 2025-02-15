package com.capstone.hidroqu.ui.screen.login

import android.annotation.SuppressLint
import android.util.Patterns
import android.widget.Toast
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.capstone.hidroqu.R
import com.capstone.hidroqu.navigation.Screen
import com.capstone.hidroqu.ui.component.TextFieldForm
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.hidroqu.ui.viewmodel.AuthViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel()
) {
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    fun validateForm(): Boolean {
        emailError = if (!Patterns.EMAIL_ADDRESS.matcher(emailValue)
                .matches()
        ) "Format email tidak valid" else null
        passwordError = if (passwordValue.isBlank()) "Password tidak boleh kosong" else null
        return emailError == null && passwordError == null
    }

    val context = LocalContext.current
    val isLoading by viewModel.isLoading

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.hidroponikbg),
            contentDescription = "Hidroponik Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Masuk dulu, yuk!",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LoginForm(
                        email = emailValue,
                        password = passwordValue,
                        onEmailChanged = {
                            emailValue = it
                            emailError = null
                        },
                        onPasswordChanged = {
                            passwordValue = it
                            passwordError = null
                        },
                        emailError = emailError,
                        passwordError = passwordError
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ForgotPasswordButton(navHostController = navHostController)
                    Spacer(modifier = Modifier.height(32.dp))

                    LoginButton(
                        navHostController = navHostController,
                        onLogin = {
                            if (validateForm()) {
                                viewModel.loginUser(
                                    emailValue,
                                    passwordValue,
                                    context = context,
                                    onSuccess = { loginResponse ->
                                        navHostController.navigate(Screen.HomeRoute.route) {
                                            popUpTo(Screen.Login.route) { inclusive = true }
                                        }
                                        isSuccess = true

                                        Toast.makeText(context, "Halo:D", Toast.LENGTH_SHORT).show()
                                    },
                                    onError = {
                                        isSuccess = false
                                        Toast.makeText(context, "Kesalahan! Akun Anda tidak ditemukan;-;", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RegisterButton(navHostController = navHostController)

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
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

@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    emailError: String?,
    passwordError: String?
) {
    var showPassword by remember { mutableStateOf(false) }
    var passwordLengthError by remember { mutableStateOf<String?>(null) }
    var isTyping by remember { mutableStateOf(false) }

    LaunchedEffect(password) {
        isTyping = password.isNotEmpty()
        passwordLengthError = if (isTyping && password.length < 8) "Password harus lebih dari 8 karakter" else null
    }

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
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = passwordError != null || (isTyping && passwordLengthError != null),
        errorMessage = passwordError ?: passwordLengthError,
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    painter = painterResource(id = if (showPassword) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                    contentDescription = if (showPassword) "Sembunyikan Kata Sandi" else "Tampilkan Kata Sandi"
                )
            }
        }
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
        onClick = {
            navHostController.navigate(Screen.Register.route)
        }
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
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Lupa password",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .clickable {
                    navHostController.navigate(Screen.ForgotPassword.route)
                }
                .padding(vertical = 8.dp, horizontal = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginScreen(
        navHostController = NavHostController(context = LocalContext.current),
    )
}
