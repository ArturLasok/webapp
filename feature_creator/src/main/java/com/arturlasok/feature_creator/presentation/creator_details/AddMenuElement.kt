package com.arturlasok.feature_creator.presentation.creator_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.arturlasok.feature_core.presentation.components.AlertButton
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.CreatorDataState

@Composable
fun AddMenuElement(
    creatorDataState: CreatorDataState,
    iconList:  List<Pair<String, ImageVector>>,
    onDismiss: () -> Unit,
    text: Int,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    darkTheme: Boolean,
    setSelectedMenuToken:(token:String) -> Unit,
    addRouteToThisMenu:(routeToken: String, placeToken:String) -> Unit,

    ) {

    val selectedPageToken = rememberSaveable {
        mutableStateOf("")
    }
    val pagesList = rememberSaveable {
        creatorDataState.projectPagesList.filterNot {
            creatorDataState.projectPageMenuList.any {menu->
                it.wLayoutRouteToken == menu.wMenuRoute
            }
        }
    }
    val textInfo = rememberSaveable {
        if(pagesList.isNotEmpty()) {
            text
        }
        else {
            R.string.creator_editmenualertNoRoutes
        }
    }
    AlertDialog(
        backgroundColor = MaterialTheme.colors.background,
        onDismissRequest = { onDismiss() },
        text = {
            Column {
                Text(UiText.StringResource(textInfo, "asd").asString(),style = MaterialTheme.typography.h3, textAlign = TextAlign.Justify)

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                   PagesLazyRow(
                       darkTheme = darkTheme,
                       creatorDataState = creatorDataState,
                       pageList = pagesList,
                       iconList = iconList,
                       selectedPageToken = selectedPageToken.value,
                       setSelectedPageToken ={ token -> selectedPageToken.value = token  }
                   )
                }
            }

        },
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
                if(pagesList.isNotEmpty()) {
                    AlertButton(
                        buttonEnabled = selectedPageToken.value.isNotEmpty(),
                        buttonText = UiText.StringResource(
                            R.string.creator_addOneMenuElement,
                            "asd"
                        )
                            .asString(),
                        textPadding = 2.dp,
                        buttonAction = {
                            setSelectedMenuToken("")
                            addRouteToThisMenu(
                                selectedPageToken.value,
                                creatorDataState.projectSelectedPageToken.value
                            )
                        },
                        modifier = Modifier
                    )
                }
            }
        },
        properties = DialogProperties(dismissOnBackPress = dismissOnBackPress,dismissOnClickOutside = dismissOnClickOutside)

    )
}



