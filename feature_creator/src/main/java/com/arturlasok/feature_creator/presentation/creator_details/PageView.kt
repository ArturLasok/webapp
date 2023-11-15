package com.arturlasok.feature_creator.presentation.creator_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import com.arturlasok.feature_core.data.datasource.api.model.WebMenu
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.util.ColorType
import com.arturlasok.feature_core.util.ExtraColors
import com.arturlasok.feature_core.util.Shimmer
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.model.CreatorDataState
import com.arturlasok.feature_creator.model.ProjectInteractionState
import com.arturlasok.feature_creator.model.ProjectMenuViewState

@Composable
fun PageView(
    darkTheme: Boolean,
    navigateToMenuByToken: (token: String) -> Unit,
    navigateTo: (route: String) -> Unit,
    navigateUp: () -> Unit,
    pageList: List<WebLayout>,
    getPagesState: ProjectInteractionState,
    setAddMenu: (str: String) -> Unit,
    menuViewFraction: Float,
    setMenuViewFraction: (newState: ProjectMenuViewState) -> Unit,
    deleteOneMenu: (webMenu: WebMenu) -> Unit,
    creatorDataState: CreatorDataState,
    reorderMenu:()->Unit,
    iconList: List<Pair<String, ImageVector>>,
) {
    val onePage = creatorDataState.projectPagesList.find {
        it.wLayoutRouteToken == creatorDataState.projectSelectedPageToken.value
    }
    AnimatedVisibility(
        visible = creatorDataState.projectGetPagesState.value == ProjectInteractionState.Idle
                && creatorDataState.projectMenuLoadingState.value == ProjectInteractionState.Idle
                && creatorDataState.projectDeleteMenuState.value == ProjectInteractionState.Idle
                && creatorDataState.projectReorderMenuState.value == ProjectInteractionState.Idle
                && creatorDataState.projectGetAllModulesState.value == ProjectInteractionState.Idle,
        exit = fadeOut(
            animationSpec = tween(delayMillis = 0, durationMillis = 0)
        ),
        enter = fadeIn(
            animationSpec = tween(
                delayMillis = 0,
                easing = FastOutSlowInEasing,
                durationMillis = 300
            )
        )
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 6.dp,
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .padding(start = 0.dp, end = 12.dp, bottom = 12.dp)
                .padding(top = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                //.verticalScroll(rememberScrollState())
                ,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //Content Column
                Column(
                    modifier = Modifier
                        .background(
                            ExtraColors(
                                type = ColorType.DESIGNONE,
                                darktheme = darkTheme
                            )
                        )
                        .fillMaxWidth()
                        .padding(6.dp),
                    verticalArrangement = Arrangement.Top,

                ) {

                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.width(36.dp)) {

                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable(onClick = { setMenuViewFraction(creatorDataState.projectMenuViewState.value) })
                                ) {
                                    Icon(
                                        imageVector = if(creatorDataState.projectMenuViewState.value==ProjectMenuViewState.Short) { Icons.Filled.Menu } else { Icons.Filled.MenuOpen },
                                        UiText.StringResource(
                                            com.arturlasok.feature_creator.R.string.creator_addmenushort,
                                            "asd"
                                        ).asString(),
                                        tint = MaterialTheme.colors.onBackground,
                                        modifier = Modifier.width(32.dp),
                                    )
                                }

                        }
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                            Row() {

                                if (onePage != null) {
                                    Text(
                                        text = onePage.wLayoutPageName.uppercase() + " [ ",
                                        style = MaterialTheme.typography.h5
                                    )
                                    Text(
                                        text = " Edit ",
                                        style = MaterialTheme.typography.h5,
                                        textDecoration = TextDecoration.Underline,
                                        modifier = Modifier.clickable(onClick = {
                                            navigateTo(Screen.EditPageScreen.routeWithArgs + "?pageId=${onePage._id.toString()}")
                                        })
                                    )
                                    Text(text = " ] ", style = MaterialTheme.typography.h5)
                                } else {
                                    Text(
                                        text = "Home Screen".uppercase(),
                                        style = MaterialTheme.typography.h5
                                    )
                                }
                            }
                    }
                    }
                }
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //Menu Column
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(fraction = menuViewFraction)
                            .fillMaxHeight()
                    ) {
                        when(creatorDataState.projectInsertMenuState.value) {
                            is ProjectInteractionState.Interact -> {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp)
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            is ProjectInteractionState.Idle -> {
                                if(creatorDataState.projectPageMenuList.size==0) {
                                    IconButton(
                                        onClick = { setAddMenu("new") },
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .height(60.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(

                                                Icons.Filled.AddCard,
                                                UiText.StringResource(
                                                    com.arturlasok.feature_creator.R.string.creator_addmenushort,
                                                    "asd"
                                                ).asString(),
                                                tint = MaterialTheme.colors.onBackground,
                                                modifier = Modifier.width(32.dp),
                                            )
                                            Text(
                                                text =   UiText.StringResource(
                                                    com.arturlasok.feature_creator.R.string.creator_addmenushort,
                                                    "asd"
                                                ).asString().uppercase(),
                                                style = MaterialTheme.typography.h6
                                            )
                                        }
                                    }
                                } else {
                                    if (creatorDataState.projectMenuViewState.value == ProjectMenuViewState.Open) {
                                        //open menu edit
                                        if(creatorDataState.projectPageMenuList.joinToString("-") { it._id.toString() }==
                                            creatorDataState.projectPageOriginalSortedList.toList().map {
                                                it._id.toString()
                                            }.joinToString("-")
                                            ) {
                                            IconButton(
                                                onClick = { setAddMenu("addelement") },
                                                modifier = Modifier
                                                    .padding(2.dp)
                                                    .height(60.dp)
                                                    .fillMaxWidth()

                                            ) {
                                                Column(
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Icon(

                                                        Icons.Filled.PlaylistAdd,
                                                        UiText.StringResource(
                                                            com.arturlasok.feature_creator.R.string.creator_addelementomenu,
                                                            "asd"
                                                        ).asString(),
                                                        tint = MaterialTheme.colors.onBackground,
                                                        modifier = Modifier.width(32.dp),
                                                    )
                                                    Text(
                                                        text = UiText.StringResource(
                                                            com.arturlasok.feature_creator.R.string.creator_addelementomenu,
                                                            "asd"
                                                        ).asString().uppercase(),
                                                        style = MaterialTheme.typography.h6
                                                    )
                                                }
                                            }
                                        }
                                        else {
                                            IconButton(
                                                onClick = {
                                                //save new menu
                                                    reorderMenu()
                                                },
                                                modifier = Modifier
                                                    .padding(2.dp)
                                                    .height(60.dp)
                                                    .fillMaxWidth()

                                            ) {
                                                Column(
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Icon(

                                                        Icons.Filled.Save,
                                                        UiText.StringResource(
                                                            com.arturlasok.feature_creator.R.string.creator_savemenuorder,
                                                            "asd"
                                                        ).asString(),
                                                        tint = MaterialTheme.colors.error,
                                                        modifier = Modifier.width(32.dp),
                                                    )
                                                    Text(
                                                        color = MaterialTheme.colors.error,
                                                        text = UiText.StringResource(
                                                            com.arturlasok.feature_creator.R.string.creator_savemenuorder,
                                                            "asd"
                                                        ).asString().uppercase(),
                                                        style = MaterialTheme.typography.h6
                                                    )
                                                }
                                            }
                                        }
                                        //---
                                    } else {
                                        IconButton(
                                            onClick = { setMenuViewFraction(creatorDataState.projectMenuViewState.value) },
                                            modifier = Modifier
                                                .padding(2.dp)
                                                .height(60.dp)
                                                .fillMaxWidth()

                                        ) {
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Icon(

                                                    Icons.Filled.MenuBook,
                                                    UiText.StringResource(
                                                        com.arturlasok.feature_creator.R.string.creator_editmenushort,
                                                        "asd"
                                                    ).asString(),
                                                    tint = MaterialTheme.colors.onBackground,
                                                    modifier = Modifier.width(32.dp),
                                                )
                                                Text(
                                                    text = UiText.StringResource(
                                                        com.arturlasok.feature_creator.R.string.creator_editmenushort,
                                                        "asd"
                                                    ).asString().uppercase(),
                                                    style = MaterialTheme.typography.h6
                                                )
                                            }
                                        }
                                    }
                                }

                            }
                            else -> {

                            }
                        }
                        when(creatorDataState.projectMenuLoadingState.value) {
                            is ProjectInteractionState.Interact -> {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp)
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            else -> {
                                MenuListForOnePage(
                                    projectPageMenuList = creatorDataState.projectPageMenuList,
                                    projectPageList = creatorDataState.projectPagesList,
                                    org = creatorDataState.projectPageOriginalSortedList.toList().map {
                                        it._id.toString()
                                    }.joinToString("-"),
                                    enabled = creatorDataState.projectMenuViewState.value is ProjectMenuViewState.Open,
                                    iconList = iconList,
                                    creatorDataState = creatorDataState.projectMenuViewState,
                                    deleteOneMenu = { webMenu ->  deleteOneMenu(webMenu)},
                                ) { token -> navigateToMenuByToken(token) }
                            }
                        }

                    }

                    //Spacer
                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 10.dp, bottom = 10.dp)
                            .width(1.dp)
                            .background(
                                Color.Gray
                            )
                    )
                    //Content Column
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        IconButton(
                            onClick = {
                                if (onePage != null) {
                                    navigateTo(Screen.ModuleScreen.routeWithArgs+
                                            "?pageId=${onePage._id.toString().substringAfter("oid=").substringBefore("}")}")
                                }
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

                                    Icons.Filled.DataArray,
                                    UiText.StringResource(
                                        R.string.core_navBack,
                                        "asd"
                                    ).asString(),
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.width(32.dp),
                                )
                                Text(
                                    text = "Add Content".uppercase(),
                                    style = MaterialTheme.typography.h6
                                )
                            }
                        }
                    }

                }

            }
        }
    }
    if(creatorDataState.projectGetAllModulesState.value!=ProjectInteractionState.Idle
        || creatorDataState.projectGetPagesState.value!=ProjectInteractionState.Idle
        || creatorDataState.projectMenuLoadingState.value!=ProjectInteractionState.Idle
        || creatorDataState.projectReorderMenuState.value!=ProjectInteractionState.Idle
        || creatorDataState.projectDeleteMenuState.value!=ProjectInteractionState.Idle
        ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 6.dp,
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .padding(start = 0.dp, end = 12.dp, bottom = 12.dp)
                .padding(top = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .background(
                        Shimmer(
                            targetValue = 1300f, showShimmer = true, color = ExtraColors(
                                type = ColorType.DESIGNONE,
                                darktheme = darkTheme
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
            }
        }
    }
}