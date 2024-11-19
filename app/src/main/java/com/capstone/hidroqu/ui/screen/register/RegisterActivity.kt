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
    val isNameValid by derivedStateOf { nameValue.isNotBlank() }
    val isEmailValid by derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() }
    val isPasswordValid by derivedStateOf { passwordValue.isNotBlank() }
    val isFormValid by derivedStateOf { isNameValid && isEmailValid && isPasswordValid }

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            SimpleLightTopAppBar(title = "Daftar", navController = navHostController,)
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
                    .padding(20.dp)){
                RegisterForm(
                    name = nameValue,
                    email = emailValue,
                    password = passwordValue,
                    onNameChanged = { nameValue = it },
                    onEmailChanged = { emailValue = it },
                    onPasswordChanged = { passwordValue = it },
                    isNameValid = isNameValid,
                    isEmailValid = isEmailValid,
                    isPasswordValid = isPasswordValid
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Register Button
                RegisterButton(
                    navController = navHostController,
                    isFormValid = isFormValid
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Login Redirect Button
                LoginRedirectButton(navController = navHostController,)
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
    isNameValid: Boolean,
    isEmailValid: Boolean,
    isPasswordValid: Boolean
) {
    TextFieldForm(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = onNameChanged,
        label = "Nama",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        isError = !isNameValid,
        errorMessage = if (!isNameValid) "Nama tidak boleh kosong" else null
    )
    Spacer(modifier = Modifier.height(8.dp))
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
fun RegisterButton(
    navController: NavHostController,
    isFormValid: Boolean,
    onRegister: (() -> Unit)? = null
) {
    Button(
        onClick = {
            onRegister?.invoke()
            if (isFormValid) {
                navController.navigate(Screen.Login.route)
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
    }
}
