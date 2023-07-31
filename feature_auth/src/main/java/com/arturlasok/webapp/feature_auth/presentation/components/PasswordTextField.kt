package com.arturlasok.webapp.feature_auth.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.util.UiText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordTextField(
    authPassword: String,
    setAuthPassword:(password: String) -> Unit,
    passwordVisibility: Boolean,
    setPasswordVisibility:(newVal: Boolean)-> Unit) {


    val textField = remember { mutableStateOf(TextFieldValue(text = authPassword)) }
    fun isValidPassword(str: String): Boolean {
        return str.length>10
    }
    val isValidPassword = remember { if(isValidPassword(authPassword) && authPassword.isNotEmpty()) mutableStateOf(true) else mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        isError = !isValidPassword.value && textField.value.text.isNotEmpty(),
        label = {
            if(!isValidPassword.value && textField.value.text.isNotEmpty()) {
                Text(UiText.StringResource(R.string.auth_validPassLike,"asd").asString())
            } else {
                Text(UiText.StringResource(R.string.auth_yourPassword,"asd").asString())
            }

                },
        value = textField.value,
        onValueChange = { newInput ->
            textField.value =  newInput
            isValidPassword.value = isValidPassword(textField.value.text)
            setAuthPassword(newInput.text)
                        },
        shape = MaterialTheme.shapes.large,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = if(isValidPassword.value) {  Color.Green } else { MaterialTheme.colors.primary.copy(alpha = 0.7f) },
            unfocusedLabelColor =   MaterialTheme.colors.primary.copy(alpha = 0.7f) ,
            focusedBorderColor = if(isValidPassword.value) { Color.Green } else { MaterialTheme.colors.primary },
            ),
        textStyle = TextStyle.Default.copy(fontWeight = FontWeight.Medium),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if(passwordVisibility) { VisualTransformation.None } else { PasswordVisualTransformation() },
        keyboardActions = KeyboardActions(onDone = {

            keyboardController?.hide(); focusManager.clearFocus(true)

        }),
        trailingIcon = {
            Row() {
                Spacer(modifier = Modifier.width(0.dp))
                    IconButton(onClick = {
                        setPasswordVisibility(!passwordVisibility)
                    },
                        modifier = Modifier
                            .padding(start = 0.dp, end = 0.dp),
                    ) {

                        Icon(
                            if (passwordVisibility) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            },
                            contentDescription = UiText.StringResource(R.string.auth_visibility,"asd").asString(),

                            tint = Color.Gray
                        )
                    }
                    IconButton(onClick = {
                        textField.value = TextFieldValue(text = "")
                        isValidPassword.value = false
                        setAuthPassword("")
                    },modifier = Modifier
                        .padding(start = 0.dp, end = 0.dp),
                    ) {
                        Icon(
                            Icons.Filled.Clear, UiText.StringResource(R.string.auth_clear,"asd").asString(),

                            tint = Color.Gray
                        )

                    }


            }
        }

    )
}