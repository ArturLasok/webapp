package com.arturlasok.feature_creator.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.zIndex
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.ModuleDataState
import com.arturlasok.feature_creator.util.ActionTextModule
import com.arturlasok.feature_creator.util.IconsForTextModule
import com.arturlasok.feature_creator.util.TextStringAlignToComposeTextAlign

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextModuleText(
    moduleDataState: ModuleDataState,
    setOpenTextModuleText:(text:String) -> Unit,
    setOpenTextModuleAction:(action: ActionTextModule) -> Unit,
    ) {
    val iconsForTextModule = IconsForTextModule().returnIcons()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val textField = remember { mutableStateOf(TextFieldValue(text = moduleDataState.projectOpenTextModule.wText)) }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .zIndex(1.0f)
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 10.dp)
            .horizontalScroll(
                rememberScrollState()
            )
    ) {
        iconsForTextModule.onEach {toolIcon->

            Column(
                modifier = Modifier.zIndex(1.0f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {

                    setOpenTextModuleAction(toolIcon.third)

                }) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box() {
                            Icon(
                                imageVector = toolIcon.second,
                                contentDescription = UiText.StringResource(toolIcon.first,"asd").asString(),
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .width(24.dp)
                                )
                        }
                        Text(UiText.StringResource(toolIcon.first,"asd").asString(), style = MaterialTheme.typography.h5)
                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(1.dp)
                )
            }

        }

    }
    OutlinedTextField(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
            .fillMaxWidth()
            .height(200.dp),
        isError = false,
        label = {

           Text(text =UiText.StringResource(R.string.creator_textmodule_textLabel,"asd").asString())
        },
        value = textField.value,
        onValueChange = { newInput ->
            if(newInput.text.length<22500) {
                textField.value = newInput
                setOpenTextModuleText(newInput.text)
            }
        },
        shape = MaterialTheme.shapes.large,
        singleLine = false,
        maxLines = 20,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = if(false) {  Color.Green } else { MaterialTheme.colors.primary.copy(alpha = 0.7f) },
            unfocusedLabelColor =   MaterialTheme.colors.primary.copy(alpha = 0.7f) ,
            focusedBorderColor = if(false) { Color.Green } else { MaterialTheme.colors.primary },
        ),
        textStyle = TextStyle.Default.copy(fontWeight = FontWeight.Medium, textAlign = TextStringAlignToComposeTextAlign(
            align = moduleDataState.projectOpenTextModule.wTextAlign
        )),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Default
        ),
        keyboardActions = KeyboardActions(onDone = {

            keyboardController?.hide(); focusManager.clearFocus(true)

        }),
        trailingIcon = {
            Column(verticalArrangement = Arrangement.Top) {
                IconButton(onClick = {
                    keyboardController?.hide(); focusManager.clearFocus(true)
                }) {
                    Icon(
                        Icons.Filled.KeyboardHide, UiText.StringResource(R.string.creator_textmodule_textHideKey,"asd").asString(),
                        tint = Color.Gray
                    )

                }
                Spacer(modifier = Modifier.height(20.dp))
                IconButton(onClick = {
                    textField.value = TextFieldValue(text = "")
                    //isValidEmail.value = false
                    setOpenTextModuleText("")
                }) {
                    Icon(
                        Icons.Filled.Clear, UiText.StringResource(R.string.creator_textmodule_textClear,"asd").asString(),
                        tint = Color.Red
                    )

                }


            }

        }

    )

}