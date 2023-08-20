package com.arturlasok.feature_core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

@Composable
fun UserLogoCircle(letter: String, letterSize: TextUnit, color:String, colorsecond: String, size: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(0.dp)
            .size(size.dp, size.dp)
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    listOf(Color(color.toColorInt()),Color(colorsecond.toColorInt()) )
                )
            )
    )
    {
        AnimatedVisibility(
            visible = letter !="",
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing,
                    delayMillis = 500
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                )
            )


        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.padding(0.dp)
            )
            {


                Text(
                    text = letter.uppercase(),
                    color = MaterialTheme.colors.surface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 1.dp),
                    style = MaterialTheme.typography.h1.copy(fontSize = letterSize)

                )

            }
        }

    }
}