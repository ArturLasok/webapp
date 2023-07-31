package com.arturlasok.feature_core.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.util.UiText

@Composable
fun TopBack(isHome: Boolean, routeLabel: String ="", onBack:() ->Unit) {
    if (isHome) {
        Text(text="Home", color = MaterialTheme.colors.onSurface)
    } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { onBack() },
                modifier = Modifier.padding(0.dp)
            ) {
                Icon(
                    Icons.Filled.ArrowBack, UiText.StringResource(R.string.core_navBack,"asd").asString(),
                    tint = MaterialTheme.colors.onSurface,
                )

            }
            Text(text = routeLabel, color = MaterialTheme.colors.onSurface)
        }
    }

}