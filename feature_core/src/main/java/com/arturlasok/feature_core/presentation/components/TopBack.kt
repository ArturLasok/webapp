package com.arturlasok.feature_core.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.util.UiText

@Composable
fun TopBack(isHome: Boolean, routeLabel: String ="", onBack:() ->Unit, onHome:() ->Unit) {
    if (isHome) {
        Text(text="Home", color = MaterialTheme.colors.onSurface)
    } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { onHome() },
                modifier = Modifier
                    .padding(0.dp)
                    .width(38.dp)
            ) {
                Icon(
                    Icons.Filled.Home, UiText.StringResource(R.string.core_navBack,"asd").asString(),
                    tint = MaterialTheme.colors.onSurface,
                )

            }
            IconButton(
                onClick = { onBack() },
                modifier = Modifier
                    .padding(end = 5.dp)
                    .width(38.dp)
            ) {
                Icon(
                    Icons.Filled.ArrowBack, UiText.StringResource(R.string.core_navBack,"asd").asString(),
                    tint = MaterialTheme.colors.onSurface,
                )

            }
            val orientationCorrect = if(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) 20 else 0
            Row(
                modifier = Modifier
                    .size(((LocalConfiguration.current.screenWidthDp/2)-76+orientationCorrect).dp)
                    .padding(top = 0.dp, bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Column() {
                    Row(Modifier.weight(0.7f).padding(bottom = 3.dp), verticalAlignment = Alignment.Bottom) {
                    Text(text = routeLabel, color = MaterialTheme.colors.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    Row(Modifier.weight(0.3f)) {
                        Spacer(modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth(0.9f)
                            .background(Color.White))
                        Spacer(modifier = Modifier
                            .height(18.dp)
                            .width(1.dp)
                            .background(Color.White)
                        )
                    }

                }
                
            }
        }
    }

}