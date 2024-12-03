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
    trailingIcon: @Composable (() -> Unit)? = null
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
            } else null,
            trailingIcon = trailingIcon
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