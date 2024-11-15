package com.capstone.hidroqu.ui.login

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.capstone.hidroqu.R
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.capstone.hidroqu.component.TextFieldForm
import com.capstone.hidroqu.ui.register.isEmailValid
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@SuppressLint("UnrememberedMutableState")

@Composable
fun LoginActivity(
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var emailValue by remember { mutableStateOf(email) }
    var passwordValue by remember { mutableStateOf(password) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    val isFormValid = isEmailValid && isPasswordValid

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
                .clickable {
                    keyboardController?.hide()
                },
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        text = "Masuk dulu, yuk!",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Login Form
                    LoginForm(
                        email = emailValue,
                        password = passwordValue,
                        onEmailChanged = onEmailChanged,
                        onPasswordChanged = onPasswordChanged,
                        isEmailValid = isEmailValid,
                        isPasswordValid = isPasswordValid
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "atau",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.outline,
                        ),
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    GoogleButton {
                        onRegisterClicked()
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Login Button
                    LoginButton(
                        isFormValid = isFormValid,
                        onLoginClicked = {
                            isEmailValid = emailValue.isNotBlank() && isEmailValid(emailValue)
                            isPasswordValid = passwordValue.isNotBlank()
                            if (isFormValid) {
                                onLoginClicked()
                            }
                        }
                    )
                    //register
                    RegisterButton {
                        onRegisterClicked()
                    }
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
    isEmailValid: Boolean,
    isPasswordValid: Boolean
) {
    // Input Email
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = {
            onEmailChanged(it)
        },
        label = "Email",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = !isEmailValid,
        errorMessage = if (!isEmailValid) "Format email tidak valid" else null
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Input Password
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChange = {
            onPasswordChanged(it)
        },
        label = "Password",
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = !isPasswordValid,
        errorMessage = if (!isPasswordValid) "Password tidak boleh kosong" else null
    )
}

@Composable
fun LoginButton(
    isFormValid: Boolean,
    onLoginClicked: () -> Unit,
) {
    Button(
        onClick = onLoginClicked,
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
            text = "Masuk",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun RegisterButton(
    onRegisterClicked: () -> Unit
) {
    TextButton(onClick = onRegisterClicked) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Belum punya akun? Daftar.",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@Composable
fun GoogleButton(
    onRegisterClicked: () -> Unit
) {
    TextButton(
        onClick = onRegisterClicked,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(100.dp)
            ),
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google Logo",
                modifier = Modifier.size(16.dp),
                contentScale = ContentScale.Fit
            )


            Spacer(modifier = Modifier.width(8.dp))

            Text(
                textAlign = TextAlign.Center,
                text = "Masuk dengan Google",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HidroQuTheme {
        LoginActivity(
            email = "",
            password = "",
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            onRegisterClicked = {},
        )
    }
}
