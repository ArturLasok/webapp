package com.arturlasok.webapp.feature_auth.presentation.auth_messages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.util.getDarkBoolean
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MessageList(
    darkTheme: Int,
    fbAuth: FirebaseAuth,
) {
    val isDark: Boolean = getDarkBoolean(isSystemInDark = isSystemInDarkTheme(), darkTheme)

    AnimatedVisibility(
        visible = true,
        exit = fadeOut(
            animationSpec = tween(delayMillis = 1000)
        ),
        enter = fadeIn(
            animationSpec = tween(delayMillis = 0)
        )
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 20.dp,
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                .fillMaxWidth()
                .padding(top = 0.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Messsages")
            }
        }
    }

}