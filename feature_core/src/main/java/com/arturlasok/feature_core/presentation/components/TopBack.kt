package com.arturlasok.feature_core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBack(onBack:() ->Unit) {
    Text(text=" ( BACK ) ", modifier = Modifier.clickable(onClick = { onBack() }).padding(end = 4.dp), color = MaterialTheme.colors.onSecondary)
}