package com.arturlasok.feature_creator.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TextStringWeightToComposeTextWeight(weight:String) : FontWeight {
    return when(weight) {
        "bold" -> FontWeight.Bold
        "normal" -> FontWeight.Normal
        else -> {
            FontWeight.Normal
        }
    }
}