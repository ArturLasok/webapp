package com.arturlasok.feature_core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.navigation.Screen

@Composable
fun TopAuth(
    navigateTo: (route: String) -> Unit,
) {
    Text("Auth",
        modifier = Modifier.clickable(onClick = {
            navigateTo(
                Screen.AuthScreen.route)}).padding(end = 4.dp))
}