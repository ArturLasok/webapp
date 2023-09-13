package com.arturlasok.webapp.feature_auth.presentation.auth_onemessage

import androidx.compose.foundation.background
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.presentation.components.DeleteIcon
import com.arturlasok.feature_core.presentation.components.UserLogoCircle
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.webapp.feature_auth.model.ProfileInteractionState
import com.arturlasok.webapp.feature_auth.presentation.auth_messages.MessagesViewModel
import com.arturlasok.webapp.feature_auth.util.DateTimeType
import com.arturlasok.webapp.feature_auth.util.TimeMilisecondsTo
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OneMessagesScreen(
    oneMessagesViewModel: OneMessagesViewModel = hiltViewModel(),
    messagesViewModel: MessagesViewModel = hiltViewModel(),
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
        oneMessagesViewModel.getOneMessageFromRoom()


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
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    )
                    {
                        if (oneMessagesViewModel.oneMessage.value.dMessage_title.isEmpty()) {
                            Text(
                                text = UiText.StringResource(
                                    R.string.auth_oneMessage_notExists,
                                    "asd"
                                )
                                    .asString(),
                                style = MaterialTheme.typography.h3,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(top = 30.dp, start = 10.dp, end = 10.dp, bottom = 30.dp)
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ){
                               Row {
                                    Column(
                                        Modifier.padding(10.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        UserLogoCircle(
                                            letter = oneMessagesViewModel.oneMessage.value.dMessage_author_mail.substring(
                                                0,
                                                1
                                            ) ?: "@",
                                            letterSize = 24.sp,
                                            color = MaterialTheme.colors.onSurface,
                                            colorsecond = MaterialTheme.colors.onSurface,
                                            size = 36
                                        )
                                    }
                                    Column(
                                        Modifier.padding(0.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        Text(
                                            text = UiText.StringResource(R.string.auth_from,"asd").asString()+" " + oneMessagesViewModel.oneMessage.value.dMessage_author_mail + "\n"
                                                    + UiText.StringResource(R.string.auth_to,"asd").asString()+" " + oneMessagesViewModel.oneMessage.value.dMessage_user_mail + "\n",
                                            style = MaterialTheme.typography.h4,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier.padding(
                                                top = 10.dp,
                                                start = 0.dp,
                                                end = 10.dp,
                                                bottom = 2.dp
                                            )

                                        )
                                    }
                                }
                               Column {
                                   DeleteIcon(tint = MaterialTheme.colors.primary) {
                                       if(dualScreen) {
                                       messagesViewModel.getMessagesGlobalState().itemToDeleteSelection.value=oneMessagesViewModel.oneMessage.value._did.toString()
                                       messagesViewModel.messageToDeleteId.value = oneMessagesViewModel.oneMessage.value._did.toString()
                                       messagesViewModel.deleteOneState.value = ProfileInteractionState.Interact(action = {})
                                       }
                                       else {
                                           messagesViewModel.getMessagesGlobalState().itemToDeleteSelection.value=oneMessagesViewModel.oneMessage.value._did.toString()
                                           messagesViewModel.messageToDeleteId.value = oneMessagesViewModel.oneMessage.value._did.toString()
                                           navigateTo(Screen.MessagesScreen.routeWithArgs + "?sent=false&delete=${oneMessagesViewModel.oneMessage.value._did.toString()}")
                                       }
                                   }
                               }

                            }
                            Text(
                                text = TimeMilisecondsTo(
                                    DateTimeType.DATE_MEDIUM,
                                    oneMessagesViewModel.oneMessage.value.dMessage_added
                                ) + " " + TimeMilisecondsTo(
                                    DateTimeType.TIME_H_AND_MIN,
                                    oneMessagesViewModel.oneMessage.value.dMessage_added),
                                style = MaterialTheme.typography.h4,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(top = 2.dp, start = 10.dp, end = 10.dp, bottom = 2.dp)
                              )


                            Text(
                                text = oneMessagesViewModel.oneMessage.value.dMessage_title,
                                style = MaterialTheme.typography.h2,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 2.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(1.dp)
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colors.primary)
                            )
                            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                                Text(
                                    text = oneMessagesViewModel.oneMessage.value.dMessage_content,
                                    style = MaterialTheme.typography.h3,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }

                        }
                    }
                }
            }

        }
    }
}