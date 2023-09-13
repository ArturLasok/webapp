package com.arturlasok.webapp.feature_auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
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
fun AuthButton(
    buttonText: String,
    textPadding: Dp,
    buttonAction:() ->Unit,
    buttonEnabled: Boolean = true,
    modifier :Modifier) {
    OutlinedButton(
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = MaterialTheme.colors.primary,

            backgroundColor = MaterialTheme.colors.primary,

        ),
        elevation = ButtonDefaults.elevation(defaultElevation = 10.dp),
        enabled = buttonEnabled,
        onClick = { buttonAction() },
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        ) {

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = buttonText,
                    modifier = Modifier.padding(start = textPadding, end = textPadding),
                    style = MaterialTheme.typography.h3,
                    color = if(buttonEnabled) {Color.White } else { MaterialTheme.colors.primary }
                )
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .width(textPadding + textPadding)
                    .height(20.dp)) {
                    if(!buttonEnabled) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 1.dp,
                            //backgroundColor = Color.DarkGray,
                            modifier = Modifier.width((20.dp))
                        )
                    }
                }

            }
    }
}
