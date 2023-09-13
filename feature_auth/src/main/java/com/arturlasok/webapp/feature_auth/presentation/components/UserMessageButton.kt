package com.arturlasok.webapp.feature_auth.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun UserMessageButton(
    messageType: UserMessageType,
    buttonText: String,
    textPadding: Dp,
    buttonAction:() ->Unit,
    buttonEnabled: Boolean = true,
    buttonVisible: Boolean = true,
    modifier : Modifier
) {
    val styleBackground = when(messageType) {
        UserMessageType.NORMAL -> { MaterialTheme.colors.surface }
        UserMessageType.IMPORTANT -> { MaterialTheme.colors.error }
    }
    val styleColor = when(messageType) {
        UserMessageType.NORMAL -> { MaterialTheme.colors.onSurface }
        UserMessageType.IMPORTANT -> { MaterialTheme.colors.onError }
    }
    AnimatedVisibility(
        visible = buttonVisible,
        exit = fadeOut(
        animationSpec = tween(delayMillis = 1000)),
        enter = fadeIn(
            animationSpec = tween(delayMillis = 1000))) {
        OutlinedButton(
            elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
            colors = ButtonDefaults.buttonColors(
                disabledBackgroundColor = styleColor,

                backgroundColor =  styleColor,

                ),

            enabled = buttonEnabled,
            onClick = { buttonAction() },
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
        ) {
            Box(contentAlignment = Alignment.Center) {

            Text(
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                text= buttonText,
                maxLines = 1,
                modifier = Modifier.padding(start = textPadding,end = textPadding),
                style = MaterialTheme.typography.h4,
                color = if(buttonEnabled) styleBackground else styleColor
            )


                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)) {
                    if(!buttonEnabled) {
                        CircularProgressIndicator(
                            color = styleBackground,
                            strokeWidth = 1.dp,
                            //backgroundColor = Color.DarkGray,
                            modifier = Modifier.width((20.dp))
                        )
                    }
                }

            }
        }
    }


}