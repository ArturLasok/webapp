package com.arturlasok.feature_core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Reply
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.util.UiText

@Composable
fun ReplyIcon(tint: Color, action:() ->Unit,) {

    IconButton(
        onClick = { action() },
        modifier = Modifier
            .padding(end = 5.dp)
            .width(38.dp)
    ) {
        Icon(
            Icons.Filled.Reply,
            UiText.StringResource(R.string.core_replyIcon, "asd").asString(),
            tint = tint,
        )

    }

}