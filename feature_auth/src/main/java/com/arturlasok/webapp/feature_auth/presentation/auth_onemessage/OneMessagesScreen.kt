package com.arturlasok.webapp.feature_auth.presentation.auth_onemessage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.webapp.feature_auth.presentation.auth_messages.MessageList
import com.arturlasok.webapp.feature_auth.presentation.auth_messages.MessagesViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OneMessagesScreen(
    oneMessagesViewModel: OneMessagesViewModel = hiltViewModel(),
    topBack: @Composable () -> Unit,
    topEnd: @Composable () -> Unit,
    mesId: String,
    dualScreen: Boolean,
    navigateTo: (route: String) -> Unit,
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
    //snackbar controller
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val scaffoldState = rememberScaffoldState()
    val dataStoreDarkTheme = oneMessagesViewModel.darkFromStore().collectAsState(initial = 0)

    LaunchedEffect(key1 = true, block = {
        if(FirebaseAuth.getInstance().currentUser==null) {
            navigateTo(Screen.StartScreen.route)
        }
        oneMessagesViewModel.getOneMessageFromRoom(mesId)

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
                Text(oneMessagesViewModel.oneMessage.value.dMessage_title)
            }
        }
    }
}