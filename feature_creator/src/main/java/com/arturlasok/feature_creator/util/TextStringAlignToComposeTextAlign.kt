package com.arturlasok.feature_creator.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TextStringAlignToComposeTextAlign(align:String) : TextAlign {
    return when(align) {
        "left" -> {
            TextAlign.Left
        }
        "right" -> {
            TextAlign.Right
        }
        "center" -> {
            TextAlign.Center
        }
        "justify" -> {
            TextAlign.Justify
        }
        else -> { TextAlign.Left }
    }
}