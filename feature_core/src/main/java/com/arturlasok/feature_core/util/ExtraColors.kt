package com.arturlasok.feature_core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ExtraColors(type: ColorType, darktheme:Boolean) : Color {
    return when(type) {
        ColorType.DESIGNONE -> {
            if(darktheme) {
                Color.Magenta.copy(alpha = 0.5f)
            }
            else {
                Color.Yellow
            }
        }
        else -> {
            Color.Transparent
        }
    }
}
enum class ColorType {
    DESIGNONE,OTHER
}