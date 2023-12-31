package com.arturlasok.feature_creator.presentation.creator_addpage

import android.util.Log
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopSettings
import com.arturlasok.feature_core.util.SnackType
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.getDarkBoolean
import com.arturlasok.feature_core.util.snackMessage
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.components.CreatorTextField
import com.arturlasok.feature_creator.components.IconsLazyRow
import com.arturlasok.feature_creator.components.SubmitButton
import com.arturlasok.feature_creator.model.ProjectInteractionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddPageScreen(
    addPageViewModel: AddPageViewModel = hiltViewModel(),
    isSecondScreen: Boolean,
    isInDualMode: Boolean,
    navigateTo: (route: String) -> Unit,
    navigateUp: () -> Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier,
) {
    val dataStoreDarkTheme = addPageViewModel.darkFromStore().collectAsState(initial = 0)
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val scaffoldState = rememberScaffoldState()
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val newPageDataState = addPageViewModel.newPageDataState.value
    LaunchedEffect(key1 = newPageDataState.newPageInsertState.value, block = {
        when (newPageDataState.newPageInsertState.value) {
            is ProjectInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message = if (addPageViewModel.haveNetwork()) {
                        (newPageDataState.newPageInsertState.value as ProjectInteractionState.Error).message
                    } else {
                        UiText.StringResource(R.string.creator_nonetwork, "asd")
                            .asString(addPageViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(
                        com.arturlasok.feature_core.R.string.core_ok,
                        "asd"
                    ).asString(addPageViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                addPageViewModel.setNewPageInsertState(ProjectInteractionState.Idle)
            }

            is ProjectInteractionState.OnComplete -> {

                navigateTo(Screen.DetailsScreen.route)
                addPageViewModel.setNewPageInsertState(ProjectInteractionState.Idle)
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
                    Text(text = newPageDataState.toString())
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
                    animationSpec = tween(delayMillis = 300)
                ),
                enter = fadeIn(
                    animationSpec = tween(
                        delayMillis =0 ,
                        easing = FastOutSlowInEasing,
                        durationMillis = 300
                    )
                )
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 20.dp,
                    color = MaterialTheme.colors.background,
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                        .padding(top = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Text(
                            text = UiText.StringResource(R.string.creator_addpageinfo, "asd").asString(),
                            style = MaterialTheme.typography.h3
                        )
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp))
                        IconsLazyRow(
                            darkTheme = getDarkBoolean(isSystemInDarkTheme(),dataStoreDarkTheme.value),
                            iconsList = addPageViewModel.iconList,
                            selectedIconName = addPageViewModel.newPageDataState.value.newPageIconName,
                            setSelectedIconName = addPageViewModel::setNewPageIconName
                        )
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp))
                        CreatorTextField(
                            enabled = true,
                            isValidString = { str ->
                                (str.length in (1..22))
                            },
                            isRed =remember { mutableStateOf(false) },
                            noWhiteSpace = false,
                            onlyLower = false,
                            maxStringLength = 30,
                            content =  newPageDataState.newPageName,
                            setContent = { text ->
                                addPageViewModel.setNewPageName(text)
                            },
                            label = UiText.StringResource(
                                R.string.creator_addPageLabel,
                                "asd"
                            ).asString(),
                            errorLabel = UiText.StringResource(
                                R.string.creator_addPageLabelError,
                                "asd"
                            ).asString()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        SubmitButton(
                            buttonText = UiText.StringResource(
                                R.string.creator_addPageSubmit,
                                "asd"
                            )
                                .asString(),
                            textPadding = 30.dp,
                            buttonAction = {
                                keyboardController?.hide(); focusManager.clearFocus(true)
                                addPageViewModel.setNewPageInsertState(ProjectInteractionState.Interact)
                                addPageViewModel.insertPage()
                            },
                            buttonEnabled = newPageDataState.newPageInsertState.value == ProjectInteractionState.Idle,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}