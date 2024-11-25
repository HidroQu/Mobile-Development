package com.capstone.hidroqu.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.hidroqu.ui.theme.HidroQuTheme

/**
 * Custom TextField komponen yang dapat digunakan kembali
 * @param value nilai text yang ditampilkan
 * @param onValueChange callback ketika nilai berubah
 * @param label label untuk text field
 * @param modifier modifier untuk styling
 * @param visualTransformation transformasi visual untuk input (contoh: password)
 * @param keyboardOptions opsi keyboard
 * @param isError menampilkan status error
 * @param errorMessage pesan error yang ditampilkan
 * @param leadingIcon icon di depan text field (optional)
 * @param trailingIcon icon di belakang text field (optional)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldForm(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    labelTextStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    singleLine: Boolean = true,  // Tambahkan parameter untuk singleLine
    maxLines: Int = 1,
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(label, style = labelTextStyle) // Gunakan gaya label yang dapat disesuaikan
            },
            modifier = modifier,
            singleLine = singleLine,
            maxLines = maxLines,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorTextColor = MaterialTheme.colorScheme.error
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            isError = isError,
            supportingText = if (isError && !errorMessage.isNullOrEmpty()) {
                { Text(text = errorMessage, color = MaterialTheme.colorScheme.error) }
            } else null
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun TextFieldPreview() {
    HidroQuTheme {
        TextFieldForm(
            value = "Sample Text",
            onValueChange = {},
            label = "Label"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HidroQuTextFieldErrorPreview() {
    HidroQuTheme {
        TextFieldForm(
            value = "Error Text",
            onValueChange = {},
            label = "Error Label",
            isError = true,
            errorMessage = "This is an error message"
        )
    }
}