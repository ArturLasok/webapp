package com.arturlasok.feature_core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.util.UiText

@Composable
fun TopMessages(navigateTo: (route: String) -> Unit,) {
    IconButton(
        onClick = {   navigateTo(Screen.MessagesScreen.route) },
        modifier = Modifier.padding(0.dp)
    ) {
        Icon(
            Icons.Filled.Message,
            UiText.StringResource(R.string.core_settings,"asd").asString(),
            tint = MaterialTheme.colors.onSurface,
        )

    }

}