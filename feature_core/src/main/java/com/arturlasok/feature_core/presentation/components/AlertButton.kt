package com.arturlasok.feature_core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AlertButton(
    buttonText: String,
    textPadding: Dp,
    buttonAction:() ->Unit,
    buttonEnabled: Boolean = true,
    modifier :Modifier) {

    OutlinedButton(
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = MaterialTheme.colors.surface,

            backgroundColor = MaterialTheme.colors.background,

            ),
        border = BorderStroke(1.dp,MaterialTheme.colors.primary),
        enabled = buttonEnabled,
        onClick = { buttonAction() },
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
    ) {
        Text(text = buttonText, style = MaterialTheme.typography.h3, modifier = Modifier.padding(textPadding))
    }
}
