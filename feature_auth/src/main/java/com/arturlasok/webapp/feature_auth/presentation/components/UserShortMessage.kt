package com.arturlasok.webapp.feature_auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.util.UiText
import com.google.firebase.auth.FirebaseUser

@Composable
fun UserShortMessage(
    modifier: Modifier,
    messageFromToAvailable: Boolean,
    messageType: UserMessageType,
    messageFrom: String,
    messageTo: FirebaseUser,
    messageText: String,
    messageButtons: @Composable ()-> Unit,
) {
    val styleBackground = when(messageType) {
        UserMessageType.NORMAL -> { MaterialTheme.colors.surface }
        UserMessageType.IMPORTANT -> { Color(0xFFBA1A1A) }
    }
    val styleColor = when(messageType) {
        UserMessageType.NORMAL -> { MaterialTheme.colors.onSurface }
        UserMessageType.IMPORTANT -> { MaterialTheme.colors.onError }
    }
    Surface(
        color = styleBackground,
        contentColor = styleColor,
        shape = MaterialTheme.shapes.medium,
        elevation = 10.dp,
        modifier = modifier

    ) {

        Column {
            if(messageFromToAvailable) {
                Row(modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 2.dp)) {
                    Text(
                        text = UiText.StringResource(R.string.auth_from, "").asString() + " ",
                        style = MaterialTheme.typography.h3,
                    )
                    Text(
                        text = messageFrom,
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(modifier = Modifier.padding(start = 12.dp, top = 0.dp, bottom = 0.dp)) {
                    Text(
                        text = UiText.StringResource(R.string.auth_to, "").asString() + " ",
                        style = MaterialTheme.typography.h3,


                        )
                    Text(
                        text = messageTo.email.toString(),
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = messageText,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(12.dp)
            )

            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                messageButtons()
            }
        }


    }


}
enum class UserMessageType {
    NORMAL,IMPORTANT
}