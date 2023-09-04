package com.arturlasok.webapp.feature_auth.presentation.auth_messages

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.AlertButton
import com.arturlasok.feature_core.presentation.components.DefaultAlert
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.util.SnackType
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.snackMessage
import com.arturlasok.webapp.feature_auth.model.ProfileInteractionState
import com.arturlasok.webapp.feature_auth.presentation.auth_onemessage.OneMessagesViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun MessagesScreen(
    messagesViewModel: MessagesViewModel = hiltViewModel(),
    oneMessagesViewModel: OneMessagesViewModel = hiltViewModel(),
    topBack: @Composable () -> Unit,
    topEnd: @Composable () -> Unit,
    currentRoute: String,
    navigateTo: (route: String) -> Unit,
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
    //snackbar controller
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val scaffoldState = rememberScaffoldState()
    val dataStoreDarkTheme = messagesViewModel.darkFromStore().collectAsState(initial = 0)

    LaunchedEffect(key1 = true, block = {
        if(FirebaseAuth.getInstance().currentUser==null) {
            navigateTo(Screen.StartScreen.route)
        }
        messagesViewModel.getAllMessagesFromKtor()
    })
    if(messagesViewModel.deleteOneState.value is ProfileInteractionState.Interact) {
        DefaultAlert(
            onDismiss = {
                messagesViewModel.deleteOneState.value = ProfileInteractionState.Idle
            },
            title = "",
            text = UiText.StringResource(com.arturlasok.feature_auth.R.string.auth_deleteMessage_question, "asd")
                .asString(messagesViewModel.applicationContext.applicationContext),
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround)
                {

                    AlertButton(
                        buttonText = UiText.StringResource(R.string.core_yes,"asd").asString(),
                        textPadding = 2.dp,
                        buttonAction = {
                          messagesViewModel.deleteOneMessage(messagesViewModel.messageToDeleteId.value)
                          messagesViewModel.deleteOneState.value = ProfileInteractionState.Idle
                        },
                        modifier = Modifier
                    )
                    AlertButton(
                        buttonText = UiText.StringResource(R.string.core_no,"asd").asString(),
                        textPadding = 2.dp,
                        buttonAction = {
                            messagesViewModel.messageToDeleteId.value = "";
                            messagesViewModel.deleteOneState.value = ProfileInteractionState.Idle
                                       },
                        modifier = Modifier
                    )

                }},
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            alertOpen = true,
            changeAlertState = {}
        )
    }
    LaunchedEffect(key1 = messagesViewModel.deleteOneState.value, block = {
        when(messagesViewModel.deleteOneState.value) {
            is ProfileInteractionState.IsSuccessful -> {
                snackMessage(
                    snackType = SnackType.NORMAL,
                    message = (messagesViewModel.deleteOneState.value as ProfileInteractionState.IsSuccessful).message,
                    actionLabel = UiText.StringResource(com.arturlasok.feature_auth.R.string.auth_ok, "asd")
                        .asString(messagesViewModel.applicationContext.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                (messagesViewModel.deleteOneState.value as ProfileInteractionState.IsSuccessful).action.invoke()
            }
            is ProfileInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message =
                    if(messagesViewModel.haveNetwork()) {
                        (messagesViewModel.deleteOneState.value as ProfileInteractionState.Error).message
                    }
                    else
                    {
                        UiText.StringResource(com.arturlasok.feature_auth.R.string.auth_nonetwork, "asd")
                            .asString(messagesViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(com.arturlasok.feature_auth.R.string.auth_ok, "asd")
                        .asString(messagesViewModel.applicationContext.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                (messagesViewModel.deleteOneState.value as ProfileInteractionState.Error).action.invoke()
            }
            else -> {
                
            }
            
        }
    })
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost =  { scaffoldState.snackbarHostState },
        topBar = {
            //Back button //Top menu
            Row(modifierTopBar,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                //Front
                Row {
                    topBack()
                }
                //End
                Row {
                    topEnd()
                }
            }
        },
        bottomBar ={
            Column {

            }

        },
        floatingActionButton = {

            FloatingActionButton(
                onClick = { navigateTo(Screen.AddMessageScreen.route) },
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier
                    .padding(end = 44.dp, bottom = 24.dp)
                    .alpha(0.8f)

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center)
                {

                Text(
                    text="Add new\nmessage",
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                }

            }

        }
    ) { paddingValues ->
        Box(modifier = modifierScaffold.padding(paddingValues)) {
            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() },
                modifier = Modifier
                    .zIndex(1.0f)
                    .padding(
                        top = 1.dp
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                //.verticalScroll(rememberScrollState())
                ,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 10.dp,
                    color = MaterialTheme.colors.background,
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                        .fillMaxWidth()
                        .fillMaxSize()
                        .padding(top = 0.dp)
                ) {
                    when(messagesViewModel.getKtorMessagesState.value) {
                        is ProfileInteractionState.Interact ->{
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally)
                            {
                                LinearProgressIndicator(color = MaterialTheme.colors.primary, modifier = Modifier.height(2.dp))
                            }
                        }
                        is ProfileInteractionState.OnComplete -> {
                            val anim = remember { mutableStateOf(0) }
                            LaunchedEffect(key1 = true){
                                delay(100)
                                anim.value = 1
                            }
                            AnimatedVisibility(
                                visible = anim.value == 1,
                                exit = fadeOut(
                                    animationSpec = tween(delayMillis = 1000)
                                ),
                                enter = fadeIn(
                                    animationSpec = tween(delayMillis = 0, easing = FastOutSlowInEasing, durationMillis = 500)
                                )
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    MessagesTabRow(
                                        currentTabPosition = messagesViewModel.getCurrentMailTabPos().value,
                                        setCurrentTabPosition = { pos ->
                                            messagesViewModel.setCurrentMailTabPos(
                                                pos
                                            )
                                        })
                                    MessageList(
                                        darkTheme = dataStoreDarkTheme.value,
                                        fbAuth = messagesViewModel.getFireAuth(),
                                        messagesList = messagesViewModel.messageList.value.filter {
                                            if(messagesViewModel.getCurrentMailTabPos().value==0) {
                                                it.dMessage_author_mail != (messagesViewModel.getFireAuth().currentUser?.email
                                                    ?: "unknown")
                                            } else {
                                                it.dMessage_author_mail == (messagesViewModel.getFireAuth().currentUser?.email
                                                    ?: "unknown")
                                            }

                                        },
                                        deleteAction = { messageId ->
                                            messagesViewModel.messageToDeleteId.value =
                                                messageId; messagesViewModel.deleteOneState.value =
                                            ProfileInteractionState.Interact(action = {})
                                        },
                                        reAction = { messageId ->
                                            Log.i(TAG, "Nav to add with $messageId")
                                            navigateTo(Screen.AddMessageScreen.routeWithArgs + "?contextId=${messageId}")
                                        },
                                        navigateTo = { route ->
                                            if (currentRoute.contains("OneMessageScreen")) {
                                                Log.i(TAG, "Get one mess from room")
                                                oneMessagesViewModel.getOneMessageFromRoom(
                                                    route.substringAfter(
                                                        "mesId="
                                                    )
                                                )
                                            } else {
                                                Log.i(TAG, "Navigate with args")
                                                navigateTo(route)
                                            }

                                        }

                                    )
                                }
                            }
                            }
                        else ->
                        {

                        }
                    }

                }
            }
        }
    }
}