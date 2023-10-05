package com.arturlasok.feature_creator.presentation.creator_editpage

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.LinearProgressIndicator
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
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.AlertButton
import com.arturlasok.feature_core.presentation.components.DefaultAlert
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.presentation.components.DeleteIcon
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopSettings
import com.arturlasok.feature_core.util.SnackType
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.snackMessage
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.components.CreatorTextField
import com.arturlasok.feature_creator.components.SubmitButton
import com.arturlasok.feature_creator.model.ProjectInteractionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditPageScreen(
    editPageViewModel: EditPageViewModel = hiltViewModel(),
    isSecondScreen: Boolean,
    isInDualMode: Boolean,
    pageId: String,
    navigateTo: (route: String) -> Unit,
    navigateUp: () -> Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier,
) {
    val editPageDataState = editPageViewModel.editPageDataState.value
    val dataStoreDarkTheme = editPageViewModel.darkFromStore().collectAsState(initial = 0)
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val scaffoldState = rememberScaffoldState()
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true, block = {
        if(editPageViewModel.editPageDataState.value.editPageId.isEmpty()) {
            editPageViewModel.getPageFromKtor(pageId)
        }
    })
    when(editPageDataState.editPageDeleteInteractionState.value) {
        is ProjectInteractionState.Interact -> {
            DefaultAlert(
                onDismiss = { editPageViewModel.setEditPageDeleteState(ProjectInteractionState.Idle) },
                title = "",
                text = UiText.StringResource(R.string.creator_editpageDelete_question, "asd")
                    .asString(editPageViewModel.applicationContext.applicationContext),
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
                                editPageViewModel.deletePage()
                                //messagesViewModel.deleteOneMessage(messagesViewModel.messageToDeleteId.value)
                                //messagesViewModel.deleteOneState.value = ProfileInteractionState.Idle
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

                                editPageViewModel.setEditPageDeleteState(
                                    ProjectInteractionState.Idle
                                )
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
        else -> {

        }
    }
    LaunchedEffect(key1 = editPageDataState, block = {
        when(editPageDataState.editPageDeleteInteractionState.value) {

            is ProjectInteractionState.OnComplete -> {
                Log.i(TAG,"oncomplete")

                    Toast.makeText(editPageViewModel.applicationContext,UiText.StringResource(
                        R.string.creator_editpageDeleted,
                        "asd"
                    ).asString(editPageViewModel.applicationContext),Toast.LENGTH_SHORT).show()
                    navigateTo(Screen.DetailsScreen.route)
                    editPageViewModel.setEditPageDeleteState(ProjectInteractionState.Idle)

            }
            is ProjectInteractionState.Error ->{
                snackMessage(
                    snackType = SnackType.ERROR,
                    message =  if(editPageViewModel.haveNetwork()) {
                        (editPageDataState.editPageDeleteInteractionState.value as ProjectInteractionState.Error).message
                    }
                    else
                    {
                        UiText.StringResource(R.string.creator_nonetwork, "asd")
                            .asString(editPageViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(com.arturlasok.feature_core.R.string.core_ok, "asd").asString(editPageViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                editPageViewModel.setEditPageDeleteState(ProjectInteractionState.Idle)
            }
            else -> {

            }
        }
        when(editPageDataState.editPageSaveInteractionState.value) {
            is ProjectInteractionState.Error ->{
                snackMessage(
                    snackType = SnackType.ERROR,
                    message =  if(editPageViewModel.haveNetwork()) {
                        (editPageDataState.editPageSaveInteractionState.value as ProjectInteractionState.Error).message
                    }
                    else
                    {
                        UiText.StringResource(R.string.creator_nonetwork, "asd")
                            .asString(editPageViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(com.arturlasok.feature_core.R.string.core_ok, "asd").asString(editPageViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                editPageViewModel.setEditPageSaveInteractionState(ProjectInteractionState.Idle)
            }
            is ProjectInteractionState.OnComplete -> {

                    Toast.makeText(editPageViewModel.applicationContext,UiText.StringResource(
                        R.string.creator_editpage_pageUpdated,
                        "asd"
                    ).asString(editPageViewModel.applicationContext),Toast.LENGTH_SHORT).show()
                    navigateTo(Screen.DetailsScreen.route)
                    editPageViewModel.setEditPageSaveInteractionState(ProjectInteractionState.Idle)

            }
            else -> {
                //nothing
            }
        }
        when(editPageDataState.editPageInteractionState.value) {
            is ProjectInteractionState.Error ->{
                snackMessage(
                    snackType = SnackType.ERROR,
                    message =  if(editPageViewModel.haveNetwork()) {
                        (editPageDataState.editPageInteractionState.value as ProjectInteractionState.Error).message
                    }
                    else
                    {
                        UiText.StringResource(R.string.creator_nonetwork, "asd")
                            .asString(editPageViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(com.arturlasok.feature_core.R.string.core_ok, "asd").asString(editPageViewModel.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                editPageViewModel.setEditPageInteractionState(ProjectInteractionState.IsCanceled)
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
                    DeleteIcon(tint = MaterialTheme.colors.onSecondary) {
                        editPageViewModel.setEditPageDeleteState(ProjectInteractionState.Interact)
                    }
                    TopSettings(navigateTo = { route -> navigateTo(route) })

                    //TopNetwork(isNetworkAvailable = startViewModel.haveNetwork())
                }


            }
        },
        bottomBar = {
            Column {
                Text("page id: $pageId", style = MaterialTheme.typography.h5)
                Text("page id: ${editPageDataState.toString()}", style = MaterialTheme.typography.h5)

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
                        delayMillis =0,
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
                            text = UiText.StringResource(R.string.creator_editpageinfo, "asd")
                                .asString(),
                            style = MaterialTheme.typography.h3
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                        )
                        if (editPageDataState.editPageDeleteInteractionState.value != ProjectInteractionState.Checking) {
                            if (editPageDataState.editPageInteractionState.value is ProjectInteractionState.OnComplete) {
                                CreatorTextField(
                                    enabled = editPageDataState.editPageInteractionState.value == ProjectInteractionState.OnComplete,
                                    isValidString = { str ->
                                        (str.length in (1..22))
                                    },
                                    isRed = remember { mutableStateOf(false) },
                                    noWhiteSpace = false,
                                    onlyLower = true,
                                    maxStringLength = 30,
                                    content = editPageDataState.editPageName.value,
                                    setContent = { text ->
                                        editPageViewModel.setEditPageName(text)
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
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            SubmitButton(
                                buttonText = UiText.StringResource(
                                    R.string.creator_editpage_updatebutton,
                                    "asd"
                                )
                                    .asString(),
                                textPadding = 30.dp,
                                buttonAction = {
                                    keyboardController?.hide(); focusManager.clearFocus(true)
                                    editPageViewModel.setEditPageSaveInteractionState(
                                        ProjectInteractionState.Interact
                                    )
                                    editPageViewModel.updatePage()
                                },
                                buttonEnabled = editPageDataState.editPageInteractionState.value == ProjectInteractionState.OnComplete
                                        && editPageDataState.editPageSaveInteractionState.value == ProjectInteractionState.Idle,
                                modifier = Modifier
                            )
                        } else {
                            //delete progress
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally)
                            {
                                Spacer(modifier = Modifier.height(16.dp))
                                LinearProgressIndicator(color = MaterialTheme.colors.primary, modifier = Modifier.height(2.dp))
                            }
                        }
                    }
                }
            }
        }
    }

}