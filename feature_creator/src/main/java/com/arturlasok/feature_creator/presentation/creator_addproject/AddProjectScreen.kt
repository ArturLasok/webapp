package com.arturlasok.feature_creator.presentation.creator_addproject

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.AlertButton
import com.arturlasok.feature_core.presentation.components.DefaultAlert
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.presentation.components.TopAuth
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopSettings
import com.arturlasok.feature_core.util.SnackType
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.getDarkBoolean
import com.arturlasok.feature_core.util.snackMessage
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.components.CreatorTextField
import com.arturlasok.feature_creator.components.SubmitButton
import com.arturlasok.feature_creator.model.ProjectInteractionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun AddProjectScreen(
    addProjectViewModel: AddProjectViewModel = hiltViewModel(),
    isSecondScreen: Boolean,
    isInDualMode: Boolean,
    navigateTo: (route: String) -> Unit,
    navigateUp: () -> Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier,
) {


    val dataStoreDarkTheme = addProjectViewModel.darkFromStore().collectAsState(initial = 0)
    val clipboardManager: androidx.compose.ui.platform.ClipboardManager = LocalClipboardManager.current
    val scaffoldState = rememberScaffoldState()
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val newProjectDataState = addProjectViewModel.newProjectDataState.value
    val newProjectInsertState = addProjectViewModel.newProjectDataState.value.newProjectInsertState.value
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


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
                    TopAuth(
                        navigateTo = { route -> navigateTo(route) },
                        fireAuth = addProjectViewModel.getFireAuth(),
                        notViewed = addProjectViewModel.numberNotViewedMessages()
                            .collectAsState(initial = 0).value
                    )
                    TopSettings(navigateTo = { route -> navigateTo(route) })
                    //TopNetwork(isNetworkAvailable = startViewModel.haveNetwork())
                }


            }
        },
        bottomBar = {
            Text("Bottom MENU")
        }
    ) { paddingValues ->
        //content
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
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    if(newProjectInsertState is ProjectInteractionState.IsSuccessful || newProjectInsertState is ProjectInteractionState.OnComplete || newProjectDataState.newProjectReady) {
                        val anim = remember { mutableStateOf(0) }
                        LaunchedEffect(key1 = true){
                            delay(100)
                            anim.value = 1
                        }

                        AnimatedVisibility(
                            visible = anim.value==1,
                            exit = fadeOut(
                                animationSpec = tween(delayMillis = 500)
                            ),
                            enter = fadeIn(
                                animationSpec = tween(delayMillis = 1500, easing = FastOutSlowInEasing, durationMillis = 600)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ProjectSuccess(
                                    newProjectDataState = newProjectDataState,
                                    openLink = {

                                        val openIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.${newProjectDataState.newProjectAddress}.${newProjectDataState.newProjectDomain}"))
                                        addProjectViewModel.applicationContext.startActivity(openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                    },
                                    shareLink = {
                                        val sendIntent: Intent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, "http://www.${newProjectDataState.newProjectAddress}.${newProjectDataState.newProjectDomain}")
                                            type = "text/plain"
                                        }
                                        val shareIntent = Intent.createChooser(sendIntent, "https://www.${newProjectDataState.newProjectAddress}.${newProjectDataState.newProjectDomain}")
                                        val context = addProjectViewModel.applicationContext

                                            context.startActivity(shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

                                    },
                                    navigateTo = {route -> navigateTo(route) },
                                    darkTheme = getDarkBoolean(isSystemInDarkTheme(),dataStoreDarkTheme.value))
                                {
                                    //copy link
                                    clipboardManager.setText(AnnotatedString(text = "https://www."+newProjectDataState.newProjectAddress+"."+newProjectDataState.newProjectDomain))
                                    Toast.makeText(addProjectViewModel.applicationContext, "copied!", Toast.LENGTH_SHORT).show()


                                }
                            }
                        }
                    }
                        AnimatedVisibility(
                            visible = (newProjectInsertState !is ProjectInteractionState.OnComplete && newProjectInsertState !is ProjectInteractionState.IsSuccessful)&& !newProjectDataState.newProjectReady,
                            exit = fadeOut(
                                animationSpec = tween(delayMillis = 0,easing = FastOutSlowInEasing, durationMillis = 500)
                            ),
                            enter = fadeIn(
                                animationSpec = tween(delayMillis = 200, easing = FastOutSlowInEasing, durationMillis = 600)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (newProjectInsertState is ProjectInteractionState.Error) {
                                    snackMessage(
                                        snackType = SnackType.ERROR,
                                        message =  if(addProjectViewModel.haveNetwork()) {
                                            newProjectInsertState.message
                                        }
                                        else
                                        {
                                            UiText.StringResource(R.string.creator_nonetwork, "asd")
                                                .asString(addProjectViewModel.applicationContext.applicationContext)
                                        },
                                        actionLabel = UiText.StringResource(com.arturlasok.feature_core.R.string.core_ok, "asd").asString(addProjectViewModel.applicationContext),
                                        snackbarController = snackbarController,
                                        scaffoldState = scaffoldState
                                    )
                                    addProjectViewModel.setNewProjectInsertState(ProjectInteractionState.Idle)
                                }
                                Text(
                                    text = UiText.StringResource(R.string.creator_detailsOfProject, "asd").asString().uppercase(),
                                    style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Medium)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = UiText.StringResource(R.string.creator_addProjectInfo, "asd").asString(),
                                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                CreatorTextField(
                                    enabled = newProjectInsertState == ProjectInteractionState.Idle,
                                    isValidString = { str ->
                                        (((str.length in (1..31)) && !addProjectViewModel.isDomainRed.value && addProjectViewModel.isSelectedAddressIsValid(
                                            str
                                        )))
                                    },
                                    isRed = addProjectViewModel.isDomainRed,
                                    onlyLower = true,
                                    maxStringLength = 30,
                                    content = newProjectDataState.newProjectAddress,
                                    setContent = { text ->
                                        addProjectViewModel.setNewProjectAddress(
                                            text.lowercase().trim()
                                        );
                                        addProjectViewModel.getDomainsFromKtor()
                                    },
                                    label = UiText.StringResource(
                                        R.string.creator_projectAddressLabel,
                                        "asd"
                                    )
                                        .asString(),
                                    errorLabel = UiText.StringResource(
                                        R.string.creator_prjectAddressLabelError,
                                        "asd"
                                    ).asString()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = UiText.StringResource(R.string.creator_addCompleteAddress, "asd").asString(),
                                    style = MaterialTheme.typography.h4,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(
                                        modifier = Modifier.weight(0.99f),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Chip(
                                            enabled = true,
                                            colors = ChipDefaults.chipColors(
                                                backgroundColor = Color.Transparent,
                                            ),
                                            border = BorderStroke(3.dp, if (addProjectViewModel.isSelectedDomainIsRed()
                                                || newProjectDataState.newProjectAddress.isEmpty()
                                                || newProjectDataState.newProjectDomain.isEmpty()
                                                || addProjectViewModel.isDomainRed.value
                                                || !addProjectViewModel.isSelectedAddressIsValid(
                                                    addProjectViewModel.newProjectDataState.value.newProjectAddress
                                                )
                                            ) {
                                                MaterialTheme.colors.error
                                            } else {
                                                Color.Green
                                            }),
                                            onClick = { },
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(16.dp),
                                                textAlign = TextAlign.Center,
                                                textDecoration = TextDecoration.Underline,
                                                text = "https://www.${addProjectViewModel.newProjectDataState.value.newProjectAddress.ifEmpty { "_______" }}.${addProjectViewModel.newProjectDataState.value.newProjectDomain.ifEmpty { "_____.___" }}",
                                                style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Medium),
                                                color = if (addProjectViewModel.isSelectedDomainIsRed()
                                                    || newProjectDataState.newProjectAddress.isEmpty()
                                                    || newProjectDataState.newProjectDomain.isEmpty()
                                                    || addProjectViewModel.isDomainRed.value
                                                    || !addProjectViewModel.isSelectedAddressIsValid(
                                                        addProjectViewModel.newProjectDataState.value.newProjectAddress
                                                    )
                                                ) {
                                                    MaterialTheme.colors.error
                                                } else {
                                                    MaterialTheme.colors.onPrimary
                                                }
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))


                                //Project domain chooser
                                when (newProjectDataState.newProjectInteractionDomainsLoadState.value) {

                                    is ProjectInteractionState.IsSuccessful -> {

                                        Row(
                                            Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = if (addProjectViewModel.isSelectedDomainIsRed()) {
                                                    UiText.StringResource(R.string.creator_domainAreRegistered, "asd").asString()
                                                } else {
                                                    UiText.StringResource(R.string.creator_domainSelectOne, "asd").asString()
                                                },
                                                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Medium),
                                                color = if (addProjectViewModel.isSelectedDomainIsRed()) {
                                                    MaterialTheme.colors.error
                                                } else {
                                                    MaterialTheme.colors.onPrimary
                                                }
                                            )
                                        }
                                        FlowRow(
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            newProjectDataState.newProjectAvailableDomainList.onEach { oneDomain ->
                                                val chipColor = if (oneDomain.wDomain_enabled) {
                                                    if (newProjectDataState.newProjectDomain == oneDomain.wDomain_address) {
                                                        MaterialTheme.colors.primary
                                                    } else {
                                                        Color.Transparent
                                                    }
                                                } else {
                                                    Color.Red
                                                }
                                                Chip(
                                                    enabled = newProjectInsertState == ProjectInteractionState.Idle,
                                                    colors = ChipDefaults.chipColors(
                                                        disabledBackgroundColor = MaterialTheme.colors.background,
                                                        disabledContentColor = Color.LightGray,
                                                        backgroundColor = chipColor,
                                                        contentColor = if (newProjectDataState.newProjectDomain == oneDomain.wDomain_address || !oneDomain.wDomain_enabled) {
                                                            Color.White
                                                        } else {
                                                            MaterialTheme.colors.onPrimary
                                                        }
                                                    ),
                                                    border = BorderStroke(
                                                        1.dp,
                                                        if (!oneDomain.wDomain_enabled) {
                                                            MaterialTheme.colors.error
                                                        } else {
                                                            MaterialTheme.colors.primary
                                                        }
                                                    ),
                                                    onClick = {
                                                        keyboardController?.hide(); focusManager.clearFocus(true)
                                                        addProjectViewModel.setNewProjectDomain(
                                                            oneDomain.wDomain_address
                                                        )
                                                    },
                                                ) {
                                                    Text(
                                                        ".${oneDomain.wDomain_address}",
                                                        style = MaterialTheme.typography.h4
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))

                                        Spacer(modifier = Modifier.height(2.dp))
                                        SubmitButton(
                                            buttonText = UiText.StringResource(
                                                R.string.creator_addProject,
                                                "asd"
                                            )
                                                .asString(),
                                            textPadding = 30.dp,
                                            buttonAction = {
                                                keyboardController?.hide(); focusManager.clearFocus(true)
                                                addProjectViewModel.setNewProjectInsertState(
                                                    ProjectInteractionState.Interact
                                                )
                                                addProjectViewModel.insertNewProject()



                                            },
                                            buttonEnabled = newProjectInsertState == ProjectInteractionState.Idle,
                                            modifier = Modifier
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = UiText.StringResource(
                                                R.string.core_policy,
                                                "asd"
                                            )
                                                .asString(),
                                            style = MaterialTheme.typography.h5
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))
                                    }

                                    is ProjectInteractionState.Interact -> {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        LinearProgressIndicator(
                                            color = MaterialTheme.colors.primary,
                                            modifier = Modifier.height(2.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = UiText.StringResource(R.string.creator_domainsCheck, "asd").asString(),
                                            style = MaterialTheme.typography.h3
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        val anim = remember { mutableStateOf(0) }
                                        LaunchedEffect(key1 = true){
                                            delay(100)
                                            anim.value = 1
                                        }
                                        AnimatedVisibility(
                                            visible = anim.value == 1,
                                            exit = fadeOut(
                                                animationSpec = tween(delayMillis = 500)
                                            ),
                                            enter = fadeIn(
                                                animationSpec = tween(delayMillis = 5000, easing = FastOutSlowInEasing, durationMillis = 1000)
                                            )
                                        ) {
                                            Text(
                                                text = UiText.StringResource(
                                                    R.string.creator_refresh,
                                                    "asd"
                                                ).asString().uppercase(),
                                                style = MaterialTheme.typography.h2,
                                                textDecoration = TextDecoration.Underline,
                                                modifier = Modifier.clickable(onClick = {
                                                    addProjectViewModel.setNewProjectInteractionDomainsLoadState(
                                                        ProjectInteractionState.Idle
                                                    )
                                                   navigateTo(Screen.AddProjectScreen.route)

                                                })
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }

                                    is ProjectInteractionState.Error -> {
                                        DefaultAlert(
                                            onDismiss = { },
                                            title = "",
                                            text = UiText.StringResource(
                                                com.arturlasok.feature_core.R.string.core_networkNotAvailable,
                                                "asd"
                                            ).asString(),
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
                                                            com.arturlasok.feature_core.R.string.core_trytoconnect,
                                                            "asd"
                                                        ).asString(),
                                                        textPadding = 2.dp,
                                                        buttonAction = {
                                                            addProjectViewModel.getDomainsFromKtor()
                                                            addProjectViewModel.setNewProjectInteractionDomainsLoadState(
                                                                ProjectInteractionState.Interact
                                                            )

                                                        },
                                                        modifier = Modifier
                                                    )
                                                }
                                            },
                                            dismissOnBackPress = false,
                                            dismissOnClickOutside = false,
                                            alertOpen = true,
                                            changeAlertState = {}
                                        )
                                    }

                                    else -> {

                                    }
                                }
                            }
                    }
                }
            }
        }
    }
}