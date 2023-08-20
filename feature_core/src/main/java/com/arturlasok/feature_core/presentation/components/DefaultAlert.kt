package com.arturlasok.feature_core.presentation.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

@Composable
fun DefaultAlert(
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
        text = { Text(text,style = MaterialTheme.typography.h3) },
        buttons = {
                    buttons()
        },
        properties = DialogProperties(dismissOnBackPress = dismissOnBackPress,dismissOnClickOutside = dismissOnClickOutside)

    )
    }
}