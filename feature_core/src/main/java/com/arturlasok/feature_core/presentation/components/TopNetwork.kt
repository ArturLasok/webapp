package com.arturlasok.feature_core.presentation.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun TopNetwork(isNetworkAvailable: Boolean) {

    Text(text= "< Internet: $isNetworkAvailable >")

}