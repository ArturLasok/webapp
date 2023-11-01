package com.arturlasok.feature_creator.presentation.creator_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.presentation.components.AlertButton
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.components.AddMenuAlert

@Composable
fun AddMenu(
    setSelectedMenuToken:(token:String) -> Unit,
    numberOfPages: Int,
    addMenu:(checkState: Boolean) -> Unit,
) {
    val checkState = rememberSaveable {
        mutableStateOf(true)
    }
    if(numberOfPages>1) {
        AddMenuAlert(
            checkState = checkState,
            newCheckState = { state: Boolean-> checkState.value = state},
            pagesNumber = numberOfPages,
            onDismiss = { setSelectedMenuToken("") },
            title = "",
            text = UiText.StringResource(R.string.creator_addMenuInfo, "asd").asString(),
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround)
                {
                    AlertButton(
                        buttonText = UiText.StringResource(
                            R.string.creator_cancel,
                            "asd"
                        )
                            .asString(),
                        textPadding = 2.dp,
                        buttonAction = {
                            setSelectedMenuToken("")
                        },
                        modifier = Modifier
                    )
                    AlertButton(
                        buttonText = UiText.StringResource(
                            R.string.creator_addmenu,
                            "asd"
                        ).asString(),
                        textPadding = 2.dp,
                        buttonAction = {
                            setSelectedMenuToken("")
                            addMenu(checkState.value)

                        },
                        modifier = Modifier
                    )
                }
            },
            dismissOnBackPress = true ,
            dismissOnClickOutside = true,
            alertOpen = true,
            changeAlertState = { }
        )
    }
    else {
        AddMenuAlert(
            checkState = checkState,
            newCheckState = { state: Boolean-> checkState.value = state},
            pagesNumber = numberOfPages,
            onDismiss = { setSelectedMenuToken("") },
            title = "",
            text = UiText.StringResource(R.string.creator_addMenuInfoZeroPages, "asd").asString(),
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center)
                {
                    AlertButton(
                        buttonText = UiText.StringResource(
                            com.arturlasok.feature_core.R.string.core_ok,
                            "asd"
                        )
                            .asString(),
                        textPadding = 2.dp,
                        buttonAction = {
                            setSelectedMenuToken("")
                        },
                        modifier = Modifier
                    )
                }
            },
            dismissOnBackPress = true ,
            dismissOnClickOutside = true,
            alertOpen = true,
            changeAlertState = { }
        )
    }

}