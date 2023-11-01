package com.arturlasok.feature_creator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.arturlasok.feature_core.util.UiText

@Composable
fun AddMenuAlert(
    checkState :MutableState<Boolean>,
    newCheckState:(state: Boolean) -> Unit,
    pagesNumber: Int,
    onDismiss:() -> Unit,
    title: String,
    text: String,
    buttons:@Composable ()->Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    alertOpen:Boolean,
    changeAlertState:(newState: Int)-> Unit) {
    if(alertOpen) {

    AlertDialog(
        backgroundColor = MaterialTheme.colors.background,
        onDismissRequest = { onDismiss() },
        //title = { if(title.isNotEmpty()) { Text(title,style = MaterialTheme.typography.h3) }},
        text = {
            Column {
                Text(text,style = MaterialTheme.typography.h3, textAlign = TextAlign.Justify)
                if(pagesNumber>0) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colors.onSecondary,
                                uncheckedColor = MaterialTheme.colors.onBackground,
                                checkmarkColor = MaterialTheme.colors.primary,
                                disabledColor = MaterialTheme.colors.onBackground,
                                // disabledIndeterminateColor=
                            ),
                            checked = checkState.value,
                            onCheckedChange = { newCheckState(!checkState.value) }
                        )
                        Spacer(modifier = Modifier.width(0.dp))
                        Text(
                            text = UiText.StringResource(
                                com.arturlasok.feature_creator.R.string.creator_addMenuAutoLinks,
                                "asd"
                            ).asString(),
                            style = MaterialTheme.typography.h3,
                            textAlign = TextAlign.Center
                        )
                    }
                } else { }

            }




               },
        buttons = {
                    buttons()
        },
        properties = DialogProperties(dismissOnBackPress = dismissOnBackPress,dismissOnClickOutside = dismissOnClickOutside)

    )
    }
}