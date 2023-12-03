package com.arturlasok.feature_creator.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun TextStringUnderlineToComposeTextUnderline(underline:String) : TextDecoration {
   return if(underline!="None") TextDecoration.Underline else TextDecoration.None
}