package com.arturlasok.feature_creator.components

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
import androidx.compose.runtime.MutableState
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
import com.arturlasok.feature_core.util.UiText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatorTextField(
    content: String,
    setContent:(text: String) -> Unit,
    label: String,
    errorLabel: String,
    isValidString:(str: String)->Boolean,
    maxStringLength: Int,
    isRed:MutableState<Boolean>,
    onlyLower: Boolean,
    enabled: Boolean,
    ) {
    fun lowerCheck(str: TextFieldValue): TextFieldValue{
        return if(onlyLower) str.copy(text = str.text.lowercase().trim(), selection = str.selection)  else str.copy(text = str.text.trim(), selection = str.selection)
    }
    val textField = remember { mutableStateOf(TextFieldValue(text = content)) }
    val isValidContent =  if(isValidString(content) && content.isNotEmpty() && !isRed.value)
    { remember { mutableStateOf(true) } } else { remember { mutableStateOf(false) } }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    OutlinedTextField(
        enabled = enabled,
        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
        isError = !isValidContent.value && textField.value.text.isNotEmpty(),
        label = {
            if(!isValidContent.value && textField.value.text.isNotEmpty()) {
                Text(errorLabel)
            } else {
                Text(label)
            }

                },
        value = textField.value,
        onValueChange = { newInput ->
            if(newInput.text.length<maxStringLength+1) {
                textField.value = lowerCheck(newInput)
                isValidContent.value = isValidString(textField.value.text)
                setContent(newInput.text.lowercase().trim())
            }
                        },
        shape = MaterialTheme.shapes.large,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = if(isValidContent.value) {  Color.Green } else { MaterialTheme.colors.primary.copy(alpha = 0.7f) },
            unfocusedLabelColor =   MaterialTheme.colors.primary.copy(alpha = 0.7f) ,
            focusedBorderColor = if(isValidContent.value) { Color.Green } else { MaterialTheme.colors.primary },
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
            IconButton(
                enabled = enabled,
                onClick = {
                textField.value = TextFieldValue(text = "")
                isValidContent.value = false
                setContent("")
            }) {
                Icon(
                    Icons.Filled.Clear, UiText.StringResource(com.arturlasok.feature_core.R.string.core_clear,"asd").asString(),
                    tint = Color.Gray
                )

            }
        }

    )
}