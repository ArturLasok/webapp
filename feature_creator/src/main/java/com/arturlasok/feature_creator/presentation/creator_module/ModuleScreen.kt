package com.arturlasok.feature_creator.presentation.creator_module

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.data.datasource.api.model.WebPageModule
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.AlertButton
import com.arturlasok.feature_core.presentation.components.DefaultAlert
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.util.ColorType
import com.arturlasok.feature_core.util.ExtraColors
import com.arturlasok.feature_core.util.Shimmer
import com.arturlasok.feature_core.util.SnackType
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.getDarkBoolean
import com.arturlasok.feature_core.util.snackMessage
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.ProjectInteractionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ModuleScreen(
    moduleViewModel: ModuleViewModel = hiltViewModel(),
    isSecondScreen: Boolean,
    isInDualMode: Boolean,
    pageId: String,
    navigateTo: (route: String) -> Unit,
    navigateUp: () -> Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier,

) {

    val moduleDataState = moduleViewModel.moduleDataState.value
    val dataStoreDarkTheme = moduleViewModel.darkFromStore().collectAsState(initial = 0).value
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val scaffoldState = rememberScaffoldState()
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    //module tools
    val toIndex: MutableState<Int?> = remember { mutableStateOf(null) }
    val selectedTool = remember { mutableStateOf("") }
    val pp = remember { mutableStateOf(0f) }
    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    val reset = remember { mutableStateOf(false) }
    val isSameAsOriginal = moduleDataState.projectModulesList.joinToString("-") { it._id.toString() } == moduleDataState.projectOriginalModuleList.joinToString("-") { it._id.toString() }


    LaunchedEffect(key1 = true, block = {
        if(moduleViewModel.moduleDataState.value.projectPageName.value.isEmpty()) {
            moduleViewModel.getPageFromKtor(pageId)
        }
    })
    when(moduleDataState.projectDeletePageModuleState.value) {
        is ProjectInteractionState.Checking -> {

                DefaultAlert(
                    onDismiss = { moduleViewModel.setProjectDeletePageModuleState(ProjectInteractionState.Idle) },
                    title = "",
                    text = UiText.StringResource(R.string.creator_deleteModule_question, "asd")
                        .asString(moduleViewModel.applicationContext.applicationContext),
                    buttons = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        )
                        {
                            AlertButton(
                                buttonText = UiText.StringResource(
                                    com.arturlasok.feature_core.R.string.core_yes,
                                    "asd"
                                ).asString(),
                                textPadding = 2.dp,
                                buttonAction = {
                                    //delete in vm
                                    moduleViewModel.deleteModule()
                                },
                                modifier = Modifier
                            )
                            AlertButton(
                                buttonText = UiText.StringResource(
                                    com.arturlasok.feature_core.R.string.core_no,
                                    "asd"
                                ).asString(),
                                textPadding = 2.dp,
                                buttonAction = {
                                    moduleViewModel.setProjectDeletePageModuleState(ProjectInteractionState.Idle)
                                },
                                modifier = Modifier
                            )
                        }
                    },
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    alertOpen = true,
                    changeAlertState = {}
                )

        }

        else -> {}
    }
    LaunchedEffect(key1 = moduleDataState, block = {

        when(moduleDataState.projectDeletePageModuleState.value) {
            is ProjectInteractionState.OnComplete -> {
                Toast.makeText(moduleViewModel.applicationContext,UiText.StringResource(
                    com.arturlasok.feature_creator.R.string.creator_pagemoduleDeleted,
                    "asd"
                ).asString(moduleViewModel.applicationContext), Toast.LENGTH_SHORT).show()
                moduleViewModel.setProjectDeletePageModuleState(ProjectInteractionState.Idle)

            }
            is ProjectInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message = if (moduleViewModel.haveNetwork()) {
                        (moduleDataState.projectDeletePageModuleState.value as ProjectInteractionState.Error).message
                    } else {
                        UiText.StringResource(R.string.creator_nonetwork, "asd")
                            .asString(moduleViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(
                        com.arturlasok.feature_core.R.string.core_ok,
                        "asd"
                    ).asString(moduleViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                moduleViewModel.setProjectDeletePageModuleState(ProjectInteractionState.Idle)
            }
            else -> {}
        }
        when(moduleDataState.projectReorderPageModuleState.value) {
            is ProjectInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message = if (moduleViewModel.haveNetwork()) {
                        (moduleDataState.projectReorderPageModuleState.value as ProjectInteractionState.Error).message
                    } else {
                        UiText.StringResource(R.string.creator_nonetwork, "asd")
                            .asString(moduleViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(
                        com.arturlasok.feature_core.R.string.core_ok,
                        "asd"
                    ).asString(moduleViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                moduleViewModel.setProjectReorderPageModuleState(ProjectInteractionState.Idle)
            }
            is ProjectInteractionState.OnComplete -> {
                Toast.makeText(moduleViewModel.applicationContext,UiText.StringResource(
                    com.arturlasok.feature_creator.R.string.creator_menureordered,
                    "asd"
                ).asString(moduleViewModel.applicationContext), Toast.LENGTH_SHORT).show()
                moduleViewModel.setProjectReorderPageModuleState(ProjectInteractionState.Idle)
            }
            else -> {
                //nothing
            }

        }

        when (moduleDataState.projectGetPageModuleState.value) {
            is ProjectInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message = if (moduleViewModel.haveNetwork()) {
                        (moduleDataState.projectGetPageModuleState.value as ProjectInteractionState.Error).message
                    } else {
                        UiText.StringResource(R.string.creator_nonetwork, "asd")
                            .asString(moduleViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(
                        com.arturlasok.feature_core.R.string.core_ok,
                        "asd"
                    ).asString(moduleViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                moduleViewModel.setProjectGetPageModuleState(ProjectInteractionState.IsCanceled)
            }

            else -> {
                //nothing
            }

        }
        when (moduleDataState.projectGetPagesState.value) {
            is ProjectInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message = if (moduleViewModel.haveNetwork()) {
                        (moduleDataState.projectGetPagesState.value as ProjectInteractionState.Error).message
                    } else {
                        UiText.StringResource(R.string.creator_nonetwork, "asd")
                            .asString(moduleViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(
                        com.arturlasok.feature_core.R.string.core_ok,
                        "asd"
                    ).asString(moduleViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                moduleViewModel.setProjectGetPageState(ProjectInteractionState.IsCanceled)
            }

            else -> {
                //nothing
            }
        }
        when (moduleDataState.projectInsertPageModuleState.value) {
            is ProjectInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message = if (moduleViewModel.haveNetwork()) {
                        (moduleDataState.projectInsertPageModuleState.value as ProjectInteractionState.Error).message
                    } else {
                        UiText.StringResource(R.string.creator_nonetwork, "asd")
                            .asString(moduleViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(
                        com.arturlasok.feature_core.R.string.core_ok,
                        "asd"
                    ).asString(moduleViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                moduleViewModel.setProjectInsertPageModuleState(ProjectInteractionState.Idle)
            }

            else -> {
                //nothing
            }
        }
    })
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        topBar = {
            if(moduleDataState.projectOpenModuleId.value.isEmpty()) {
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
                        onlyName = moduleDataState.projectOpenModuleId.value.isNotEmpty(),
                        isSecondScreen = false,
                        isInDualMode = false,
                        routeLabel = if (moduleDataState.projectOpenModuleId.value.isEmpty()) {
                            navScreenLabel
                        } else {
                            UiText.StringResource(R.string.creator_moduleContent, "asd")
                                .asString(moduleViewModel.applicationContext.applicationContext)
                        },
                        onBack = { navigateUp() })
                    { navigateTo(Screen.StartScreen.route) }
                }

                //End
                Row {
                    //TopSettings(navigateTo = { route -> navigateTo(route) })
                    //TopNetwork(isNetworkAvailable = startViewModel.haveNetwork())
                }
            }

            }
        },
        bottomBar = {

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
            val animm = remember { mutableStateOf(0) }
            LaunchedEffect(key1 = true) {
                delay(100)
                animm.value = 1
            }

            AnimatedVisibility(
                visible = animm.value == 1,
                exit = fadeOut(
                    animationSpec = tween(delayMillis = 100)
                ),
                enter = fadeIn(
                    animationSpec = tween(
                        delayMillis = 0,
                        easing = FastOutSlowInEasing,
                        durationMillis = 100
                    )
                )
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 20.dp,
                    color = MaterialTheme.colors.background,
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, bottom = 10.dp)
                        .padding(top = 0.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {

                        Box(modifier = Modifier
                            .padding(top = 0.dp)
                            .fillMaxSize()
                            .zIndex(1.0f)) {
                            if (moduleDataState.projectGetPagesState.value == ProjectInteractionState.OnComplete
                                && moduleDataState.projectInsertPageModuleState.value == ProjectInteractionState.Idle
                                && moduleDataState.projectGetPageModuleState.value == ProjectInteractionState.OnComplete
                                && moduleDataState.projectReorderPageModuleState.value == ProjectInteractionState.Idle
                                ) {
                                //ifListOfModuleIsSameAsOriginal
                                if(isSameAsOriginal) {
                                    when(moduleDataState.projectOpenModuleId.value) {

                                        ""-> {
                                            ModuleTopBar(
                                                moduleDataState = moduleDataState,
                                                dataStoreDarkTheme = dataStoreDarkTheme,
                                                icons = moduleViewModel.iconList,
                                                selectedTool = selectedTool,
                                                pp = pp,
                                                offsetX = offsetX,
                                                offsetY = offsetY,
                                                setSelectedTool = {toolName -> selectedTool.value = toolName },
                                                setResetValue = { value ->  reset.value = value; toIndex.value=null },
                                                reset = reset,
                                                addThisElement = { name ->
                                                    if (toIndex.value != null || moduleDataState.projectModulesList.isEmpty()) {
                                                        moduleViewModel.addModuleToList(
                                                            position = toIndex.value,
                                                            pageModule = WebPageModule(
                                                                _id = "name" + System.currentTimeMillis(),
                                                                wPageModuleType = name,
                                                            )
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                        else -> {
                                            EditModuleBar(
                                                moduleDataState = moduleDataState,
                                                updateOpenModuleId = moduleViewModel::setProjectOpenModuleId,
                                                deleteOneModule = { id->
                                                    moduleViewModel.setProjectDeletePageModuleState(ProjectInteractionState.Checking)
                                                    moduleViewModel.setProjectModuleIdToDelete(id) }
                                            )
                                        }


                                    }

                                }
                                else {
                                    //save new order
                                    NewModuleOrderButtons(
                                        dataStoreDarkTheme = dataStoreDarkTheme,
                                        saveNewOrder = { moduleViewModel.updateSortPageModule(moduleDataState.projectModulesList.toList(),true) },
                                        resetToPreviousOrder = { moduleViewModel.getPageFromKtor(pageId = moduleDataState.projectSelectedPageId.value) }
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(0.dp)
                                        .padding(top = 80.dp)
                                        .zIndex(0.9f),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                )
                                {
                                    if(((moduleDataState.projectModulesList.isEmpty()) || ((moduleDataState.projectModulesList.size == moduleViewModel.deletedItems.size)))
                                        && moduleDataState.projectGetPageModuleState.value == ProjectInteractionState.OnComplete) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(10.dp)
                                        )
                                        {
                                            Text(
                                                text= UiText.StringResource(R.string.creator_emptyModuleList, "asd").asString(),
                                                style = MaterialTheme.typography.h3,
                                                textAlign = TextAlign.Center
                                            )

                                        }
                                    }
                                    else {
                                        when(moduleDataState.projectOpenModuleId.value) {
                                            "" -> {
                                                ModuleContent(
                                                    offsetX = offsetX,
                                                    offsetY = offsetY,
                                                    pp = pp,
                                                    toIndex = toIndex,
                                                    setToIndex = { index -> toIndex.value = index },
                                                    reset = reset,
                                                    moduleDataState = moduleDataState,
                                                    dataStoreDarkTheme = dataStoreDarkTheme,
                                                    icons = moduleViewModel.iconList,
                                                    deleteOneModule = {id ->

                                                        moduleViewModel.setProjectDeletePageModuleState(ProjectInteractionState.Checking)
                                                        moduleViewModel.setProjectModuleIdToDelete(id)
                                                    },
                                                    deletedMessagesList = moduleViewModel.deletedItems,
                                                    setOpenModuleToId = moduleViewModel::setProjectOpenModuleId
                                                )
                                            }
                                            else -> {
                                                ModuleView(
                                                    //viewPortSize = remember { derivedStateOf { moduleLazyListState.layoutInfo.viewportSize } },
                                                    dataStoreDarkTheme = getDarkBoolean(isSystemInDarkTheme(),dataStoreDarkTheme),
                                                    backgroundColor = Color.Transparent,
                                                    moduleDataState = moduleDataState,
                                                    icons = moduleViewModel.iconList,
                                                    oneElement = moduleDataState.projectModulesList.first {
                                                        it._id.toString().substringAfter("oid=").substringBefore("}")==moduleDataState.projectOpenModuleId.value
                                                    },
                                                    index = 0,
                                                    deleteOneModule = { id->
                                                        moduleViewModel.setProjectDeletePageModuleState(ProjectInteractionState.Checking)
                                                        moduleViewModel.setProjectModuleIdToDelete(id) },
                                                    updateOpenModuleId = { id->
                                                        moduleViewModel.setProjectOpenModuleId("")
                                                    },
                                                    setOpenTextModuleText = moduleViewModel::setOpenTextModuleText,
                                                    setOpenTextModuleAction = moduleViewModel::setModuleTextAction,
                                                    setOpenTextModuleColorAction = moduleViewModel::setComponentColor,
                                                    setOpenTextModuleSettings = moduleViewModel::setComponentSettings,
                                                    setOpenTextModuleLink = moduleViewModel::setComponentLink,
                                                    makeTextModuleSnack = { textRes ->
                                                        snackMessage(
                                                            snackType = SnackType.NORMAL,
                                                            message =
                                                                UiText.StringResource(textRes, "asd")
                                                                    .asString(moduleViewModel.applicationContext.applicationContext)
                                                            ,
                                                            actionLabel = UiText.StringResource(
                                                                com.arturlasok.feature_core.R.string.core_ok,
                                                                "asd"
                                                            ).asString(moduleViewModel.applicationContext),
                                                            snackbarController = snackbarController,
                                                            scaffoldState = scaffoldState
                                                        )

                                                    }
                                                )
                                            }
                                        }

                                    }
                                }
                            } else {
                                if (moduleDataState.projectGetPagesState.value == ProjectInteractionState.IsCanceled ||
                                    moduleDataState.projectGetPageModuleState.value == ProjectInteractionState.IsCanceled
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    {
                                        IconButton(
                                            onClick = {  moduleViewModel.getPageFromKtor(pageId) },
                                            modifier = Modifier
                                                .padding(0.dp)
                                                .width(64.dp)
                                        ) {
                                            Icon(
                                                Icons.Filled.Refresh,
                                                UiText.StringResource(com.arturlasok.feature_core.R.string.core_refresh, "asd")
                                                    .asString(),
                                                tint = MaterialTheme.colors.surface,
                                            )


                                        }
                                        Text(
                                            color = MaterialTheme.colors.surface,
                                            text = UiText.StringResource(
                                                com.arturlasok.feature_core.R.string.core_refresh,
                                                "asd"
                                            ).asString().uppercase(),
                                            style = MaterialTheme.typography.h2,
                                            modifier = Modifier.clickable(onClick = {
                                                moduleViewModel.getPageFromKtor(pageId)
                                            })
                                        )
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(0.dp)
                                            .background(
                                                Shimmer(
                                                    targetValue = 1300f,
                                                    showShimmer = true,
                                                    color = ExtraColors(
                                                        type = ColorType.DESIGNONE,
                                                        darktheme = getDarkBoolean(
                                                            isSystemInDarkTheme(),
                                                            dataStoreDarkTheme
                                                        )
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
                    }
                }
            }
        }
    }
}