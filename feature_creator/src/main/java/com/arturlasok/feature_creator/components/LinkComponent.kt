package com.arturlasok.feature_creator.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.LinkModuleState
import com.arturlasok.feature_creator.model.LinkType
import com.arturlasok.feature_creator.presentation.creator_details.PagesLazyRow
import com.arturlasok.feature_creator.util.IconsForPages
import java.util.regex.Pattern

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LinkComponent(
    linkModuleState: LinkModuleState,
    dataStoreDarkTheme: Boolean,
    projectPagesList:List<WebLayout>,
    returnLinkState:(linkModuleState: LinkModuleState) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier.padding(start = 2.dp, end = 12.dp),
        elevation = 3.dp,
        shape = MaterialTheme.shapes.medium,
        color = if(dataStoreDarkTheme) {
            MaterialTheme.colors.background} else { Color(0xFFEEEEEE)
        },

        ) {
        Column(
            modifier = Modifier
                .padding(start = 0.dp)
                .fillMaxWidth()
        ) {

            when(linkModuleState.componentLinkType){
                is LinkType.TEXT_MODULE_LINK -> {

                    Text(
                        text = UiText.StringResource(
                            linkModuleState.componentRes,
                            "asd"
                        ).asString(),
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(top=10.dp, start = 10.dp)
                    )
                    if(projectPagesList.any{
                        it.wLayoutRouteToken==linkModuleState.componentValue
                    }) {

                    }
                    else {
                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                                .fillMaxWidth()
                        )
                        CreatorTextField(
                            enabled = !projectPagesList.any {
                                it.wLayoutRouteToken == linkModuleState.componentValue
                            },
                            isValidString = { str ->
                                (((str.length in (1..160)) && isLinkAddressIsValid(
                                    str
                                )))
                            },
                            isRed = remember { mutableStateOf(false) },
                            onlyLower = true,
                            maxStringLength = 160,
                            content = linkModuleState.componentValue,
                            setContent = { text ->
                                if (((text.length in (1..160)) && isLinkAddressIsValid(
                                        text
                                    ))
                                ) {
                                    returnLinkState(linkModuleState.copy(componentValue = text))
                                } else {
                                    returnLinkState(linkModuleState.copy(componentValue = ""))
                                }
                            },
                            label = UiText.StringResource(
                                R.string.creator_module_linkToOtherPage,
                                "asd"
                            )
                                .asString(),
                            errorLabel = UiText.StringResource(
                                R.string.creator_prjectAddressLabelError,
                                "asd"
                            ).asString()
                        )
                    }
                    Spacer(modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth())
                    Column(modifier = Modifier.padding(start = 12.dp, bottom = 20.dp)) {
                        PagesLazyRow(
                            darkTheme = dataStoreDarkTheme,
                            pageList = projectPagesList,
                            iconList = IconsForPages().returnIcons(),
                            selectedPageToken = linkModuleState.componentValue,
                            setSelectedPageToken = { token ->  if(token==linkModuleState.componentValue) returnLinkState(linkModuleState.copy(componentValue = "")) else returnLinkState(linkModuleState.copy(componentValue = token))}
                        )
                    }


                }
            }
        }

    }

}
fun isLinkAddressIsValid(address: String) : Boolean {
    val ADDRESS_PATTERN = Pattern.compile(
        "(https:\\/\\/www\\.|http:\\/\\/www\\.|https:\\/\\/|http:\\/\\/)?[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})(\\.[a-zA-Z]{2,})?\\/[a-zA-Z0-9]{2,}|((https:\\/\\/www\\.|http:\\/\\/www\\.|https:\\/\\/|http:\\/\\/)?[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})(\\.[a-zA-Z]{2,})?)|(https:\\/\\/www\\.|http:\\/\\/www\\.|https:\\/\\/|http:\\/\\/)?[a-zA-Z0-9]{2,}\\.[a-zA-Z0-9]{2,}\\.[a-zA-Z0-9]{2,}(\\.[a-zA-Z0-9]{2,})?"
    )
    return ADDRESS_PATTERN.matcher(address).matches()
}
