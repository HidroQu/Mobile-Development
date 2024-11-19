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

@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginActivity(
    navHostController: NavHostController,  // Accept navHostController here
    modifier: Modifier = Modifier
) {
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    val isEmailValid by derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() }
    val isPasswordValid by derivedStateOf { passwordValue.isNotBlank() }
    val isFormValid by derivedStateOf { isEmailValid && isPasswordValid }

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
                    LoginForm(
                        email = emailValue,
                        password = passwordValue,
                        onEmailChanged = { emailValue = it },
                        onPasswordChanged = { passwordValue = it },
                        isEmailValid = isEmailValid,
                        isPasswordValid = isPasswordValid
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    // Login Button
                    LoginButton(
                        navController = navHostController,  // Use navHostController here
                        isFormValid = isFormValid
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Register Button
                    RegisterButton(navController = navHostController)

                    Spacer(modifier = Modifier.height(32.dp))

                    // Google Login Button
                    GoogleButton(navController = navHostController)
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
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = onEmailChanged,
        label = "Email",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = !isEmailValid,
        errorMessage = if (!isEmailValid) "Format email tidak valid" else null
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChange = onPasswordChanged,
        label = "Password",
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = !isPasswordValid,
        errorMessage = if (!isPasswordValid) "Password tidak boleh kosong" else null
    )
}


@Composable
fun LoginButton(
    navController: NavHostController,
    isFormValid: Boolean,
    onLogin: (() -> Unit)? = null
) {
    Button(
        onClick = {
            if (isFormValid) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) {inclusive = true}
                }
            }
        },
        enabled = isFormValid,
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
    navController: NavHostController
) {
    TextButton(
        onClick = { navController.navigate(Screen.Register.route) }
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
fun GoogleButton(
    navController: NavHostController,
) {
    TextButton(
        onClick = { navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Login.route)
        } },
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