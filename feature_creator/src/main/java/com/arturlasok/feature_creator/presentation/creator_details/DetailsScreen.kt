package com.arturlasok.feature_creator.presentation.creator_details

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.AlertButton
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopSettings
import com.arturlasok.feature_core.util.SnackType
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.getDarkBoolean
import com.arturlasok.feature_core.util.snackMessage
import com.arturlasok.feature_creator.components.DefaultTabRow
import com.arturlasok.feature_creator.model.ProjectInteractionState
import kotlinx.coroutines.delay


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailsScreen(
    detailsViewModel: DetailsViewModel = hiltViewModel(),
    isSecondScreen: Boolean,
    isInDualMode: Boolean,
    navigateTo: (route: String) -> Unit,
    navigateUp: () -> Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier,
) {
    val creatorDataState = detailsViewModel.creatorDataState.value
    val dataStoreDarkTheme = detailsViewModel.darkFromStore().collectAsState(initial = 0).value
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val scaffoldState = rememberScaffoldState()
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current;

    LaunchedEffect(key1 = creatorDataState, block = {
        when(creatorDataState.projectGetPagesState.value) {
            is ProjectInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message =  if(detailsViewModel.haveNetwork()) {
                        (creatorDataState.projectGetPagesState.value as ProjectInteractionState.Error).message
                    }
                    else
                    {
                        UiText.StringResource(com.arturlasok.feature_creator.R.string.creator_nonetwork, "asd")
                            .asString(detailsViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(R.string.core_ok, "asd").asString(detailsViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
            }
            else -> {
                //nothing
            }
        }
        when(creatorDataState.projectMenuLoadingState.value) {
            is ProjectInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message =  if(detailsViewModel.haveNetwork()) {
                        (creatorDataState.projectMenuLoadingState.value as ProjectInteractionState.Error).message
                    }
                    else
                    {
                        UiText.StringResource(com.arturlasok.feature_creator.R.string.creator_nonetwork, "asd")
                            .asString(detailsViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(R.string.core_ok, "asd").asString(detailsViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
            }
            else -> {
                //nothing
            }
        }
        when(creatorDataState.projectInsertMenuState.value) {
            is ProjectInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message =  if(detailsViewModel.haveNetwork()) {
                        (creatorDataState.projectInsertMenuState as ProjectInteractionState.Error).message
                    }
                    else
                    {
                        UiText.StringResource(com.arturlasok.feature_creator.R.string.creator_nonetwork, "asd")
                            .asString(detailsViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(R.string.core_ok, "asd").asString(detailsViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                detailsViewModel.setInsertMenuState(ProjectInteractionState.Idle)
            }
            is ProjectInteractionState.OnComplete-> {
                Log.i(TAG, "ProjectInteractionState.OnComplete->")
                Toast.makeText(detailsViewModel.applicationContext,UiText.StringResource(
                    com.arturlasok.feature_creator.R.string.creator_menuadded,
                    "asd"
                ).asString(detailsViewModel.applicationContext), Toast.LENGTH_SHORT).show()
                detailsViewModel.setInsertMenuState(ProjectInteractionState.Idle)
            }
            else -> {
                //nothing
            }
        }
    })
    //Menu ADD
    when(creatorDataState.projectSelectedMenuToken.value) {

        "" -> {
            //empty -> do nothing
        }
        "new" -> {

            AddMenu(
                setSelectedMenuToken = detailsViewModel::setSelectedMenuToken,
                numberOfPages = detailsViewModel.creatorDataState.value.projectPagesList.size,
                addMenu = { checkState -> detailsViewModel.insertMenu(checkState) }
            )

        }
        "addelement" -> {

            AddMenuElement(
                darkTheme = getDarkBoolean(isSystemInDarkTheme(),dataStoreDarkTheme),
                creatorDataState = creatorDataState,
                onDismiss = { detailsViewModel.setSelectedMenuToken("") },
                text = UiText.StringResource(com.arturlasok.feature_creator.R.string.creator_editmenualertinfo, "asd").asString(),
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                setSelectedMenuToken = detailsViewModel::setSelectedMenuToken,
                addRouteToThisMenu = detailsViewModel::addRouteToThisMenu,

            )
        }
        else -> {

        }

    }


    Scaffold(

        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        topBar = {
            //Back button //Top menu
            Row(
                modifierTopBar,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {

                //Front
                Row {
                    TopBack(
                        isHome = false,
                        isSecondScreen = false,
                        isInDualMode = false,
                        routeLabel = navScreenLabel,
                        onBack = { navigateUp() })
                    { navigateTo(Screen.StartScreen.route) }
                }

                //End
                Row {
                    TopSettings(navigateTo = { route -> navigateTo(route) })
                    //TopNetwork(isNetworkAvailable = startViewModel.haveNetwork())
                }


            }
        },
        bottomBar = {
            Column {
                // LazyModulesRow(modulesList = detailsViewModel.getListOfWebModules())
                Text("ProjectID: ${detailsViewModel.creatorDataState.value.projectId.value}", style = MaterialTheme.typography.h6)
                detailsViewModel.creatorDataState.value.projectPagesList.onEach {
                    Text("Page name: ${it.wLayoutPageName} / PageId ${
                        it._id?.toString()} / ProjectId: ${it.wLayoutProjectId?.toString()}", style = MaterialTheme.typography.h6)

                }
                Text("page List: ${detailsViewModel.creatorDataState.value.projectPagesList.toString()}", style = MaterialTheme.typography.h6)
            }
        },
        drawerGesturesEnabled = false,
        drawerContent = {
            Column {
                Text("Sheet")
            }
        },

        ) { paddingValues ->
        Box(
            modifier = modifierScaffold
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() },
                modifier = Modifier
                    .zIndex(1.0f)
                    .padding(
                        top = 1.dp
                    )
            )
            Column {


                val animm = remember { mutableStateOf(0) }
                LaunchedEffect(key1 = true) {
                    delay(100)
                    animm.value = 1
                }

                AnimatedVisibility(
                    visible = animm.value == 1,
                    exit = fadeOut(
                        animationSpec = tween(delayMillis = 300)
                    ),
                    enter = fadeIn(
                        animationSpec = tween(
                            delayMillis = 0,
                            easing = FastOutSlowInEasing,
                            durationMillis = 300
                        )
                    )
                ) {
                    //MENU VIEW
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        elevation = 20.dp,
                        color = MaterialTheme.colors.background,
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp, bottom = 10.dp)
                            .padding(top = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp)
                            //.verticalScroll(rememberScrollState())
                            ,
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                DefaultTabRow(
                                    currentTabPosition = 0,
                                    setCurrentTabPosition = {},
                                    daneTab = listOf("Creator", "Details", "Preview")
                                )
                                //Text(text = "DETAILS")
                            }
                        }
                    }


                }

                val anim = remember { mutableStateOf(0) }
                LaunchedEffect(key1 = true) {
                    delay(100)
                    anim.value = 1
                }
                AnimatedVisibility(
                    visible = anim.value == 1,
                    exit = fadeOut(
                        animationSpec = tween(delayMillis = 300)
                    ),
                    enter = fadeIn(
                        animationSpec = tween(
                            delayMillis = 0,
                            easing = FastOutSlowInEasing,
                            durationMillis = 300
                        )
                    )
                ) {
                    Row {
                        //Pages list
                        PagesLazyColumn(
                            darkTheme = getDarkBoolean(isSystemInDarkTheme(),dataStoreDarkTheme),
                            navigateTo = { route -> navigateTo(route) },
                            navigateUp = { navigateUp() },
                            pageList = creatorDataState.projectPagesList,
                            creatorDataState = creatorDataState,
                            screenRefresh = detailsViewModel::screenRefresh,
                            getPagesState = creatorDataState.projectGetPagesState.value,
                            setSelectedPageToken = detailsViewModel::setSelectedPageToken,



                            )
                        //PAGE VIEW
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            PageView(
                                darkTheme = getDarkBoolean(isSystemInDarkTheme(),dataStoreDarkTheme),
                                navigateToMenuByToken = detailsViewModel::setSelectedPageToken,
                                navigateTo = { route -> navigateTo(route) },
                                navigateUp = { navigateUp() },
                                pageList = creatorDataState.projectPagesList,
                                getPagesState = creatorDataState.projectGetPagesState.value,
                                setAddMenu = detailsViewModel::setSelectedMenuToken,
                                menuViewFraction = detailsViewModel.menuViewStateToFraction(creatorDataState.projectMenuViewState.value),
                                setMenuViewFraction = { newState ->  detailsViewModel.setMenuViewState(newState) },
                                creatorDataState = creatorDataState
                            )
                        }
                    }


                }

            }

        }
    }
}
