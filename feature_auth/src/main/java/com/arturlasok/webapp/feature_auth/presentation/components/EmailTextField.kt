package com.arturlasok.webapp.feature_auth.presentation.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import java.util.regex.Pattern

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailTextField(authLogin: String, setAuthLogin:(login: String) -> Unit) {
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
    fun isValidString(str: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }
    val isValidEmail = remember { if(isValidString(authLogin) && authLogin.isNotEmpty()) mutableStateOf(true) else mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    OutlinedTextField(
        isError = !isValidEmail.value && textField.value.text.isNotEmpty(),
        label = {
            if(!isValidEmail.value && textField.value.text.isNotEmpty()) {
                Text("A valid email like: adress@example.com")
            } else {
                Text("Your email")
            }

                },
        value = textField.value,
        onValueChange = { newInput ->
            textField.value =  newInput
            isValidEmail.value = isValidString(textField.value.text)
            setAuthLogin(newInput.text)
                        },
        shape = MaterialTheme.shapes.large,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colors.primary.copy(alpha = 0.7f),
            unfocusedLabelColor = MaterialTheme.colors.primary.copy(alpha = 0.7f)
            ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {

            keyboardController?.hide(); focusManager.clearFocus(true)



        }),

    )
}