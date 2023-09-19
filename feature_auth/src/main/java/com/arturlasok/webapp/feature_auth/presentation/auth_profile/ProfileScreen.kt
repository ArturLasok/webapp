package com.arturlasok.webapp.feature_auth.presentation.auth_profile

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
import com.arturlasok.feature_core.presentation.components.TopLogOut
import com.arturlasok.feature_core.util.SnackType
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.snackMessage
import com.arturlasok.webapp.feature_auth.model.ProfileInteractionState
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    notViewed:(number: Int) ->Unit,
    topBack: @Composable () -> Unit,
    topEnd: @Composable (fbAuth: FirebaseAuth) -> Unit,
    navigateTo: (route: String) -> Unit,
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
    //snackbar controller
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val scaffoldState = rememberScaffoldState()
    val dataStoreDarkTheme = profileViewModel.darkFromStore().collectAsState(initial = 0)
     notViewed(profileViewModel.numberNotViewedMessages().collectAsState(initial = 0).value)
    val dataStoreMobileToken = profileViewModel.getMobileTokenStore().collectAsState(initial = "").value
    LaunchedEffect(key1 = true, block = {
        if(FirebaseAuth.getInstance().currentUser==null) {
            navigateTo(Screen.StartScreen.route)
        }
    })
    LaunchedEffect(
        key1 = profileViewModel.profileDataState.value.profileVerificationInteractionState.value,
        key2 = profileViewModel.profileDataState.value.profileInfoInteractionState.value,
        block = {

        listOf(
            profileViewModel.profileDataState.value.profileInfoInteractionState,
            profileViewModel.profileDataState.value.profileVerificationInteractionState)
            .onEach { interactionState->



            when (interactionState.value) {
                ProfileInteractionState.Idle -> {
                    Log.i(TAG, "profileDataStateOnEach: ${interactionState.value} IDLE")
                }

                is ProfileInteractionState.Interact -> {
                    Log.i(TAG, "profileDataStateOnEach: ${interactionState.value} INTERACT")
                    (interactionState.value as ProfileInteractionState.Interact).action.invoke()
                }

                ProfileInteractionState.OnComplete -> {

                }

                is ProfileInteractionState.IsSuccessful -> {
                    Log.i(TAG, "profileDataStateOnEach: ${interactionState.value} SUCCESSFUL")
                    snackMessage(
                        snackType = SnackType.NORMAL,
                        message = (interactionState.value as ProfileInteractionState.IsSuccessful).message,
                        actionLabel = UiText.StringResource(R.string.auth_ok, "asd")
                            .asString(profileViewModel.applicationContext.applicationContext),
                        snackbarController = snackbarController,
                        scaffoldState = scaffoldState
                    )
                    (interactionState.value as ProfileInteractionState.IsSuccessful).action.invoke()

                }

                is ProfileInteractionState.Error -> {
                    Log.i(TAG, "profileDataStateOnEach: ${interactionState.value} ERROR")
                    snackMessage(
                        snackType = SnackType.ERROR,
                        message = if(profileViewModel.haveNetwork()) { (interactionState.value as ProfileInteractionState.Error).message } else {
                            UiText.StringResource(R.string.auth_nonetwork, "asd")
                                .asString(profileViewModel.applicationContext.applicationContext)
                                                                                                                                              },
                        actionLabel = UiText.StringResource(R.string.auth_ok, "asd")
                            .asString(profileViewModel.applicationContext.applicationContext),
                        snackbarController = snackbarController,
                        scaffoldState = scaffoldState
                    )
                    (interactionState.value as ProfileInteractionState.Error).action.invoke()
                }

                else -> {

                }
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
                    topEnd(profileViewModel.getFireAuth())
                    TopLogOut({route ->

                        //clear DB
                        profileViewModel.removeAllMessagesFromRoom()
                        //navigate to login
                        navigateTo(route)


                              }, firebaseAuth = profileViewModel.getFireAuth())

                }
            }
        },
        bottomBar ={
            /*
            Column {
                Text("ProfileVer: "+profileViewModel.profileDataState.value.profileVerificationInteractionState.value.toString().substringAfter("$"))
                Text("ProfileInfo:" +profileViewModel.profileDataState.value.profileInfoInteractionState.value.toString().substringAfter("$"))
                Text("Mobile Token: ${dataStoreMobileToken}")
             }
             */


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

                //profile first login message for User
                ProfileFirstLoginMessage(
                    profileFirstLogin = profileViewModel.profileDataState.value.profileFirstLogin,
                    fbAuth = profileViewModel.getFireAuth()
                )
                //profile info
                ProfileInfo(
                    darkTheme = dataStoreDarkTheme.value,
                    fbAuth = profileViewModel.getFireAuth(),
                    profileDataState = profileViewModel.profileDataState.value,
                )
                //verification message for new User
                ProfileVerificationMessage(
                    checkVerification = profileViewModel::checkVerification,
                    sendVerificationMail = profileViewModel::resendActivationMail,
                    fbAuth = profileViewModel.getFireAuth(),
                    profileIsVerified = profileViewModel.profileDataState.value.profileVerified,
                    verificationMailButtonEnabled = profileViewModel.verificationMailButtonEnabled,
                    verificationMailButtonVisible = profileViewModel.verificationMailButtonVisible,
                    verificationCheckButtonEnabled = profileViewModel.verificationCheckButtonEnabled.value,
                )
                ProfileProjectsView(
                    navigateTo = { route -> navigateTo(route) },
                    navigateUp = {  },
                    profileViewModel.allProjects,
                    profileViewModel.haveNetwork(),
                )

            }
        }
    }
}