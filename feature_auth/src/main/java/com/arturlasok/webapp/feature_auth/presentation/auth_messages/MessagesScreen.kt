package com.arturlasok.webapp.feature_auth.presentation.auth_messages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.webapp.feature_auth.presentation.auth_onemessage.OneMessagesViewModel
import com.google.firebase.auth.FirebaseAuth

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
                    IconButton(
                        onClick = { messagesViewModel.removeAllMessagesFromRoom() },
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .width(38.dp)
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            UiText.StringResource(R.string.core_navBack, "asd").asString(),
                            tint = MaterialTheme.colors.onSurface,
                        )

                    }
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
                backgroundColor = MaterialTheme.colors.onSecondary,
                modifier = Modifier.padding(20.dp)

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center)
                {

                Text(
                    text="New \nmessage",
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("currentRoute"+currentRoute)
                MessageList(
                    darkTheme = dataStoreDarkTheme.value,
                    fbAuth = messagesViewModel.getFireAuth(),
                    messagesList = messagesViewModel.messageList.value,
                    navigateTo = { route ->
                    if(currentRoute.contains("OneMessageScreen")) {
                            Log.i(TAG, "Get one mess from room")
                            oneMessagesViewModel.getOneMessageFromRoom(route.substringAfter("mesId="))
                            }
                    else {
                        Log.i(TAG, "Navigate with args")
                        navigateTo(route)
                    }


                    }
                )
            }
        }
    }
}