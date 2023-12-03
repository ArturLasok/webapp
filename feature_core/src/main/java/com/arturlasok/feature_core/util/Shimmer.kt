package com.arturlasok.feature_core.util

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color



@Composable
fun Shimmer(showShimmer: Boolean = true,targetValue:Float = 1000f, color: Color): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            MaterialTheme.colors.background.copy(alpha = 1.0f),
            color.copy(alpha = 0.9f),
            MaterialTheme.colors.background.copy(alpha = 1.0f),
        )

        val transition = rememberInfiniteTransition(label = "Shimmer anim")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Restart
            ), label = "Shimmer anim"
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent,Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}