package com.arturlasok.webapp.feature_auth.presentation.auth_profile

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopLogOut
import com.arturlasok.feature_core.presentation.components.TopNetwork
import com.arturlasok.feature_core.util.SnackType
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.snackMessage
import com.arturlasok.webapp.feature_auth.model.ProfileInteractionState

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navigateTo: (route: String) -> Unit,
    navigateUp:()->Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
    //snackbar controller
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val scaffoldState = rememberScaffoldState()

    val interactionState = profileViewModel.profileInteractionState
    LaunchedEffect(key1 = interactionState.value, block = {
        when(interactionState.value) {
            ProfileInteractionState.Idle -> {

            }
            is ProfileInteractionState.Interact -> {
                (interactionState.value as ProfileInteractionState.Interact).action.invoke()
            }
            ProfileInteractionState.OnComplete -> {

            }
            is ProfileInteractionState.IsSuccessful ->{
                snackMessage(
                    snackType = SnackType.NORMAL,
                    message = (interactionState.value as ProfileInteractionState.IsSuccessful).message,
                    actionLabel = UiText.StringResource(R.string.auth_ok,"asd").asString(profileViewModel.applicationContext.applicationContext),
                    snackbarController =snackbarController,
                    scaffoldState =scaffoldState)
                (interactionState.value as ProfileInteractionState.IsSuccessful).action.invoke()

            }
            is ProfileInteractionState.Error -> {
                snackMessage(
                    snackType = SnackType.ERROR,
                    message = (interactionState.value as ProfileInteractionState.Error).message,
                    actionLabel = UiText.StringResource(R.string.auth_ok,"asd").asString(profileViewModel.applicationContext.applicationContext),
                    snackbarController =snackbarController,
                    scaffoldState =scaffoldState)
                (interactionState.value as ProfileInteractionState.Error).action.invoke()
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
                    TopBack(isHome = false, routeLabel = navScreenLabel, onBack = { navigateUp() })
                    { navigateTo(Screen.StartScreen.route) }
                }
                //End
                Row {
                    TopLogOut(navigateTo = { route -> navigateTo(route) }, firebaseAuth = profileViewModel.getFireAuth() )
                    TopNetwork(isNetworkAvailable = profileViewModel.haveNetwork())
                }
            }
        },
        bottomBar ={
            Text("Bottom MENU")
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

                //profile first login Message for User
                ProfileFirstLoginMessage(
                    profileFirstLogin = profileViewModel.profileDataState.value.profileFirstLogin,
                    fbAuth = profileViewModel.getFireAuth() )

                ProfileVerificationMessage(
                    checkVerification =  profileViewModel::checkVerification,
                    sendVerificationMail= profileViewModel::resendActivationMail,
                    fbAuth = profileViewModel.getFireAuth(),
                    profileIsVerified = profileViewModel.profileDataState.value.profileVerified,
                    verificationMailButtonEnabled = profileViewModel.verificationMailButtonEnabled,
                    verificationMailButtonVisible = profileViewModel.verificationMailButtonVisible,
                    verificationCheckButtonEnabled =profileViewModel.verificationCheckButtonEnabled.value,
                )

            }
        }
    }
}