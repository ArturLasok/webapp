package com.arturlasok.feature_core.util

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ExtraColors(type: ColorType, darktheme:Boolean) : Color {
    return when(type) {
        ColorType.DESIGNMENUMODULE -> {
            if(darktheme) {
                Color.DarkGray
            }
            else {
                Color.LightGray
            }
        }
        ColorType.DESIGNMENU -> {
            if(darktheme) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            }
            else {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            }
        }
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
    DESIGNMENU,DESIGNONE,DESIGNMENUMODULE,OTHER
}