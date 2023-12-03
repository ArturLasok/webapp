package com.arturlasok.feature_creator.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TextStringSizeToComposeTextSize(size:String) : TextUnit {
    return when(size) {
        "h1" -> {
            9.sp
        }
        "h2" -> {
           12.sp
        }
        "h3" -> {
            14.sp
        }
        "h4" -> {
            16.sp
        }
        "h5" -> {
            22.sp
        }
        "h6" -> {
           30.sp
        }
        else -> {
           14.sp
        }
    }
}
fun decreaseTextModuleSize(size:String) : String {
    return when(size) {
        "h1" -> { "h1" }
        "h2" -> { "h1" }
        "h3" -> { "h2" }
        "h4" -> { "h3" }
        "h5" -> { "h4" }
        "h6" -> { "h5" }
        else -> { "h3" }
    }

}
fun increaseTextModuleSize(size:String) : String {
    return when(size) {
        "h1" -> { "h2" }
        "h2" -> { "h3" }
        "h3" -> { "h4" }
        "h4" -> { "h5" }
        "h5" -> { "h6" }
        "h6" -> { "h6" }
        else -> { "h3" }
    }

}