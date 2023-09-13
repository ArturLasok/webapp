package com.arturlasok.feature_core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.util.UiText

@Composable
fun TopMessages(navigateTo: (route: String) -> Unit,notViewed: Int) {
    IconButton(
        onClick = {   navigateTo(Screen.MessagesScreen.route) },
        modifier = Modifier.padding(0.dp)
    ) {
    BadgedBox(
        badge = {
            if(notViewed>0) {
                Badge {

                    Text(
                        text = notViewed.toString(),
                        modifier = Modifier
                            .semantics {
                                contentDescription =
                                    "new notifications"
                            }
                    )
                }
            }
        }) {

            Icon(
                Icons.Filled.Message,
                UiText.StringResource(R.string.Screen_Messages,"asd").asString(),
                tint = MaterialTheme.colors.onSurface,
            )

        }
    }


}