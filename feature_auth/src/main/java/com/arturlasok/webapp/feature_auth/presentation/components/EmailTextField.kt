package com.arturlasok.webapp.feature_auth.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.util.UiText
import java.util.regex.Pattern

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailTextField(
    authLogin: String,
    setAuthLogin:(login: String) -> Unit) {

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    val textField = remember { mutableStateOf(TextFieldValue(text = authLogin)) }
    fun isValidEmailString(str: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }
    val isValidEmail = remember { if(isValidEmailString(authLogin) && authLogin.isNotEmpty()) mutableStateOf(true) else mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    OutlinedTextField(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
        isError = !isValidEmail.value && textField.value.text.isNotEmpty(),
        label = {
            if(!isValidEmail.value && textField.value.text.isNotEmpty()) {
                Text(UiText.StringResource(R.string.auth_validEmailLike,"asd").asString())
            } else {
                Text(UiText.StringResource(R.string.auth_yourEmail,"asd").asString())
            }

                },
        value = textField.value,
        onValueChange = { newInput ->
            textField.value =  newInput
            isValidEmail.value = isValidEmailString(textField.value.text)
            setAuthLogin(newInput.text)
                        },
        shape = MaterialTheme.shapes.large,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = if(isValidEmail.value) {  Color.Green } else { MaterialTheme.colors.primary.copy(alpha = 0.7f) },
            unfocusedLabelColor =   MaterialTheme.colors.primary.copy(alpha = 0.7f) ,
            focusedBorderColor = if(isValidEmail.value) { Color.Green } else { MaterialTheme.colors.primary },
            ),
        textStyle = TextStyle.Default.copy(fontWeight = FontWeight.Medium),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {

            keyboardController?.hide(); focusManager.clearFocus(true)

        }),
        trailingIcon = {
            IconButton(onClick = {
                textField.value = TextFieldValue(text = "")
                isValidEmail.value = false
                setAuthLogin("")
            }) {
                Icon(
                    Icons.Filled.Clear, UiText.StringResource(R.string.auth_clear,"asd").asString(),
                    tint = Color.Gray
                )

            }
        }

    )
}