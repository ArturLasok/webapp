package com.arturlasok.webapp.feature_auth.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.util.UiText

@Composable
fun AuthButton(
    buttonText: String,
    textPadding: Dp,
    buttonAction:() ->Unit,
    buttonEnabled: Boolean = true,
    modifier :Modifier) {
    OutlinedButton(
        enabled = buttonEnabled,
        onClick = { buttonAction() },
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        ) {
        Text(
            text= buttonText,
            modifier = Modifier.padding(start = textPadding,end = textPadding),
            style = MaterialTheme.typography.h3,
            color = Color.White
        )
    }
}
