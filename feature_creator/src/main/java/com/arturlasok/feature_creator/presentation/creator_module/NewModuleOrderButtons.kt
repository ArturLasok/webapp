package com.arturlasok.feature_creator.presentation.creator_module

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.feature_core.util.ColorType
import com.arturlasok.feature_core.util.ExtraColors
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.getDarkBoolean
import com.arturlasok.feature_creator.R

@Composable
fun NewModuleOrderButtons(saveNewOrder: () -> Unit, resetToPreviousOrder: () -> Unit, dataStoreDarkTheme: Int) {
    Column(modifier = Modifier.height(56.dp).fillMaxWidth()
    ) {

        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 6.dp,
            color = MaterialTheme.colors.background
            ,
            modifier = Modifier.padding(2.dp)
                //  .padding(top = 16.dp)
                .height(50.dp)
                .fillMaxWidth()
                .zIndex(1.0f)
        ) {


    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        IconButton(
            onClick = {
                resetToPreviousOrder()
            },
            modifier = Modifier
                .padding(2.dp)
                .height(50.dp)
                .fillMaxWidth(fraction = 0.5f)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(

                    Icons.Filled.RestartAlt,
                    UiText.StringResource(
                        R.string.creator_restoremoduleorder,
                        "asd"
                    ).asString(),
                    tint = MaterialTheme.colors.surface,
                    modifier = Modifier.width(32.dp),
                )
                Text(
                    color = MaterialTheme.colors.surface,
                    text = UiText.StringResource(
                        R.string.creator_restoremoduleorder,
                        "asd"
                    ).asString().uppercase(),
                    style = MaterialTheme.typography.h6
                )
            }
        }
        IconButton(
            onClick = {
                saveNewOrder()
            },
            modifier = Modifier
                .padding(2.dp)
                .height(50.dp)
                .fillMaxWidth()

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(

                    Icons.Filled.Save,
                    UiText.StringResource(
                        R.string.creator_savemoduleorder,
                        "asd"
                    ).asString(),
                    tint = MaterialTheme.colors.error,
                    modifier = Modifier.width(32.dp),
                )
                Text(
                    color = MaterialTheme.colors.error,
                    text = UiText.StringResource(
                        R.string.creator_savemoduleorder,
                        "asd"
                    ).asString().uppercase(),
                    style = MaterialTheme.typography.h6
                )
            }
        }

    }
        }
    }
}