package com.arturlasok.feature_creator.presentation.creator_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.util.ColorType
import com.arturlasok.feature_core.util.ExtraColors
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.model.CreatorDataState
import com.arturlasok.feature_creator.model.ProjectInteractionState

@Composable
fun PagesLazyColumn(
    darkTheme:Boolean,
    navigateTo: (route: String) -> Unit,
    navigateUp: () -> Unit,
    pageList: List<WebLayout>,
    iconList:  List<Pair<String, ImageVector>>,
    getPagesState: ProjectInteractionState,
    creatorDataState: CreatorDataState,
    screenRefresh: () -> Unit,
    setSelectedPageToken:(token:String) -> Unit,

    ) {

    val pagesColumnState = rememberLazyListState()
    //Pages
    Column(
        modifier = Modifier.fillMaxWidth(fraction = 0.25f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {


        if(getPagesState == ProjectInteractionState.Idle) {
        //Add new Page
        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 6.dp,
                color = MaterialTheme.colors.background,
                modifier = Modifier
                    .padding(start = 12.dp, end = 0.dp, bottom = 6.dp)
                    .padding(top = 0.dp)
                    .height(60.dp)
                    .fillMaxWidth(fraction = 0.90f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    IconButton(
                        onClick = { navigateTo(Screen.AddPageScreen.route) },
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxSize()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Filled.AddCircleOutline,
                                UiText.StringResource(
                                    com.arturlasok.feature_creator.R.string.creator_addPageLabel,
                                    "asd"
                                ).asString(),
                                tint = MaterialTheme.colors.onBackground,
                                modifier = Modifier.width(32.dp),
                            )
                            Text(
                                text = "Add Page".uppercase(),
                                style = MaterialTheme.typography.h6
                            )
                        }

                    }
                }
            }
            Spacer(modifier= Modifier
                .height(2.dp)
                .width(40.dp)
                .background(Color.Transparent))
        }

        //Rest of pages
        //if(getPagesState == ProjectInteractionState.Idle) {
            LazyColumn(
                state = pagesColumnState,
                modifier = Modifier.padding(bottom = 0.dp)
            ) {

                itemsIndexed(
                    items = pageList,
                    key = { _, item -> item._id?.toString() ?: "" }
                ) { index, onePage ->
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            elevation = 6.dp,
                            color = if(creatorDataState.projectSelectedPageToken.value==onePage.wLayoutRouteToken) {
                                ExtraColors(
                                    type = ColorType.DESIGNONE,
                                    darktheme = darkTheme
                                )
                            } else { MaterialTheme.colors.background },
                            modifier = Modifier
                                .padding(
                                    start = 12.dp,
                                    end = 0.dp,
                                    bottom = 6.dp
                                )
                                .padding(top = 0.dp)
                                .height(60.dp)
                                .fillMaxWidth(fraction = 0.90f)
                        ) {
                            Column(
                                modifier = Modifier,
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                IconButton(
                                    onClick = {
                                        setSelectedPageToken(onePage.wLayoutRouteToken)
                                        //navigateTo(Screen.EditPageScreen.routeWithArgs + "?pageId=${onePage._id.toString()}")

                                              },
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .fillMaxSize()

                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(

                                            iconList.find {
                                                it.first == onePage.wLayoutModuleType
                                            }?.second ?: Icons.Filled.WebAsset,
                                            UiText.StringResource(
                                                com.arturlasok.feature_creator.R.string.creator_projectPage,
                                                "asd"
                                            ).asString(),
                                            tint = MaterialTheme.colors.onBackground,
                                            modifier = Modifier.width(32.dp),
                                        )
                                        Text(
                                            text = onePage.wLayoutPageName.uppercase()
                                                .toString(),
                                            style = MaterialTheme.typography.h6,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .height(2.dp)
                                .width(40.dp)
                                .background(
                                    if (creatorDataState.projectSelectedPageToken.value == onePage.wLayoutRouteToken
                                        && creatorDataState.projectGetAllModulesState.value == ProjectInteractionState.Idle
                                        && creatorDataState.projectDeleteMenuState.value == ProjectInteractionState.Idle
                                        && creatorDataState.projectMenuLoadingState.value == ProjectInteractionState.Idle
                                    ) {
                                        ExtraColors(
                                            type = ColorType.DESIGNONE,
                                            darktheme = darkTheme
                                        )
                                    } else {
                                        Color.Transparent
                                    }
                                )
                        )
                    }
                }
            }
        }
        if(getPagesState is ProjectInteractionState.Error) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 6.dp,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            end = 0.dp,
                            bottom = 6.dp
                        )
                        .padding(top = 0.dp)
                        .height(60.dp)
                        .fillMaxWidth(fraction = 0.90f)
                ) {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        IconButton(
                            onClick = { screenRefresh() },
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxSize()

                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(

                                    Icons.Filled.Refresh,
                                    UiText.StringResource(
                                        com.arturlasok.feature_creator.R.string.creator_refreshit,
                                        "asd"
                                    ).asString(),
                                    tint = MaterialTheme.colors.onError,
                                    modifier = Modifier.width(32.dp),
                                )
                                Text(
                                    text = UiText.StringResource(com.arturlasok.feature_creator.R.string.creator_refreshit, "asd")
                                        .asString(),
                                    style = MaterialTheme.typography.h6,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.onError
                                )
                            }
                        }

                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(40.dp)
                        .background(Color.Transparent)
                )
            }
        }
        if(getPagesState == ProjectInteractionState.Interact) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 6.dp,
                    color = ExtraColors(
                        type = ColorType.DESIGNONE,
                        darktheme = darkTheme
                    ),
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            end = 0.dp,
                            bottom = 6.dp
                        )
                        .padding(top = 0.dp)
                        .height(60.dp)
                        .fillMaxWidth(fraction = 0.90f)
                ) {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        IconButton(
                            onClick = {  },
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxSize()

                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LinearProgressIndicator(color = MaterialTheme.colors.primary, modifier = Modifier
                                    .height(2.dp)
                                    .padding(start = 4.dp, end = 4.dp))
                            }
                        }

                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(40.dp)
                        .background(Color.Transparent)
                )
            }
        }
    }
}
