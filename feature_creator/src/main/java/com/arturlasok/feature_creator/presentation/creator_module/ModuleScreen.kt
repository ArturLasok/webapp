package com.arturlasok.feature_creator.presentation.creator_module

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopSettings
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
import kotlin.random.Random

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


    LaunchedEffect(key1 = true, block = {
        if(moduleViewModel.moduleDataState.value.projectPageName.value.isEmpty()) {
            moduleViewModel.getPageFromKtor(pageId)
        }
    })
    when(moduleDataState.projectGetPagesState.value) {
        is ProjectInteractionState.Error -> {
            snackMessage(
                snackType = SnackType.ERROR,
                message =  if(moduleViewModel.haveNetwork()) {
                    (moduleDataState.projectGetPagesState.value as ProjectInteractionState.Error).message
                }
                else
                {
                    UiText.StringResource(R.string.creator_nonetwork, "asd")
                        .asString(moduleViewModel.applicationContext.applicationContext)
                },
                actionLabel = UiText.StringResource(com.arturlasok.feature_core.R.string.core_ok, "asd").asString(moduleViewModel.applicationContext),
                snackbarController = snackbarController,
                scaffoldState = scaffoldState
            )
        }
        else -> {
            //nothing
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
                            if (moduleDataState.projectGetPagesState.value == ProjectInteractionState.OnComplete) {

                                ModuleTopBar(
                                    moduleDataState = moduleDataState,
                                    dataStoreDarkTheme = dataStoreDarkTheme,
                                    icons = moduleViewModel.iconList,
                                    selectedTool = selectedTool,
                                    pp = pp,
                                    offsetX = offsetX,
                                    offsetY = offsetY,
                                    setSelectedTool = {toolName -> selectedTool.value = toolName },
                                    setResetValue = { value ->  reset.value = value },
                                    reset = reset,
                                    addThisElement = { name ->
                                        moduleViewModel.addModuleToList(
                                            position= toIndex.value,
                                            layout = WebLayout(
                                                _id = "name" + System.currentTimeMillis(),
                                                wLayoutPageName = name,
                                                wLayoutSort = Random.nextLong(30,200)
                                            )
                                        )
                                    }

                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(0.dp)
                                        .padding(top = 60.dp)
                                        .zIndex(0.9f),
                                    //.verticalScroll(rememberScrollState())
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                )
                                {
                                    ModuleContent(
                                        offsetX = offsetX,
                                        offsetY = offsetY,
                                        pp = pp,
                                        toIndex = toIndex,
                                        setToIndex = {index -> toIndex.value = index },
                                        reset = reset,
                                        moduleDataState = moduleDataState,
                                        dataStoreDarkTheme = dataStoreDarkTheme,
                                        icons = moduleViewModel.iconList
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