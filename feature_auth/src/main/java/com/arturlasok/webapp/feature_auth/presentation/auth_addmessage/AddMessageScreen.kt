package com.arturlasok.webapp.feature_auth.presentation.auth_addmessage

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.util.SnackType
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.snackMessage
import com.arturlasok.webapp.feature_auth.model.ProfileInteractionState
import com.arturlasok.webapp.feature_auth.presentation.auth_messages.MessagesViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddMessageScreen(
    messagesViewModel: MessagesViewModel = hiltViewModel(),
    addMessageViewModel: AddMessageViewModel = hiltViewModel(),
    contextId: String,
    topBack: @Composable () -> Unit,
    topEnd: @Composable () -> Unit,
    navigateTo: (route: String) -> Unit,
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
    //snackbar controller
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val scaffoldState = rememberScaffoldState()
    val dataStoreDarkTheme = addMessageViewModel.darkFromStore().collectAsState(initial = 0)
    LaunchedEffect(key1 = true, block = {
        //load data if message have context
        if(contextId.isNotEmpty()) {
            addMessageViewModel.getOneMessageFromRoom(contextId)
        }
    })
    LaunchedEffect(key1 = true, block = {
        if(FirebaseAuth.getInstance().currentUser==null) {
            navigateTo(Screen.StartScreen.route)
        }

    })
    LaunchedEffect(key1 = addMessageViewModel.newMessageDataState.value.newMessageSendInteractionState.value, block = {
        when(addMessageViewModel.newMessageDataState.value.newMessageSendInteractionState.value) {
            is ProfileInteractionState.Interact -> {
                (addMessageViewModel.newMessageDataState.value.newMessageSendInteractionState.value as ProfileInteractionState.Interact).action.invoke()

            }
            is ProfileInteractionState.IsSuccessful -> {

                snackMessage(
                    snackType = SnackType.NORMAL,
                    message = (addMessageViewModel.newMessageDataState.value.newMessageSendInteractionState.value as ProfileInteractionState.IsSuccessful).message,
                    actionLabel = UiText.StringResource(R.string.auth_ok, "asd")
                        .asString(addMessageViewModel.applicationContext.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                messagesViewModel.getAllMessagesFromKtor()
                (addMessageViewModel.newMessageDataState.value.newMessageSendInteractionState.value as ProfileInteractionState.IsSuccessful).action.invoke()
            }
            is ProfileInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message =
                    if(addMessageViewModel.haveNetwork()) {
                        (addMessageViewModel.newMessageDataState.value.newMessageSendInteractionState.value as ProfileInteractionState.Error).message
                    }
                    else
                    {
                        UiText.StringResource(R.string.auth_nonetwork, "asd")
                            .asString(addMessageViewModel.applicationContext.applicationContext)
                    },
                    actionLabel = UiText.StringResource(R.string.auth_ok, "asd")
                        .asString(addMessageViewModel.applicationContext.applicationContext),
                    snackbarController = snackbarController,
                    scaffoldState = scaffoldState
                )
                (addMessageViewModel.newMessageDataState.value.newMessageSendInteractionState.value as ProfileInteractionState.Error).action.invoke()
            }
            else -> {}
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

        }
    ) { paddingValues ->
        Box(modifier = modifierScaffold.padding(paddingValues)) {
            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() },
                modifier = Modifier
                    .zIndex(1.0f)
                    .padding(top = 1.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("arg:$contextId")
                AddMessageForm(
                    setNewMessage = { message -> addMessageViewModel.setNewMessageText(message) },
                    setNewMessageTopic = { messageTopic -> addMessageViewModel.setNewMessageTopic(messageTopic)  },
                    setNewMessageContext = { mContext ->  addMessageViewModel.setNewMessageContext(mContext) },
                    darkTheme = dataStoreDarkTheme.value,
                    fbAuth = addMessageViewModel.getFireAuth(),
                    newMessageDataState = addMessageViewModel.newMessageDataState.value,
                    sendClick = { addMessageViewModel.sendMessage() }
                )

            }
        }
    }
}