package com.arturlasok.webapp.feature_auth.presentation.auth_forgot

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
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopNetwork
import com.arturlasok.feature_core.presentation.components.TopSettings
import com.arturlasok.feature_core.util.SnackType
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.snackMessage
import com.arturlasok.webapp.feature_auth.model.AuthState
import com.arturlasok.webapp.feature_auth.presentation.components.AuthButton
import com.arturlasok.webapp.feature_auth.presentation.components.EmailTextField
import kotlinx.coroutines.delay

@Composable
fun ForgotScreen(
    forgotViewModel: ForgotViewModel = hiltViewModel(),
    navigateTo: (route: String) -> Unit,
    navigateUp:()->Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier


) {
    //snackbar controller
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val authForgotDataState = forgotViewModel.authForgotDataState.value
    val authState = forgotViewModel.authState
    val scaffoldState = rememberScaffoldState()


    LaunchedEffect(key1 = authState.value, block = {
        if(authState.value== AuthState.Success) {


            snackMessage(
                snackType = SnackType.ERROR,
                message = UiText.StringResource(R.string.auth_fb_resetMailSended,"asd").asString(forgotViewModel.applicationContext.applicationContext),
                actionLabel = "OK",
                snackbarController =snackbarController,
                scaffoldState =scaffoldState)
            //navigateTo(Screen.AuthScreen.route)


        }
        if(authState.value is AuthState.AuthError) {
            snackMessage(
                snackType = SnackType.ERROR,
                message = (authState.value as AuthState.AuthError).message ?: UiText.StringResource(R.string.auth_somethingWrong,"asd").asString(forgotViewModel.applicationContext.applicationContext),
                actionLabel = "OK",
                snackbarController =snackbarController,
                scaffoldState =scaffoldState)
            delay(2000)
            forgotViewModel.setAuthState(AuthState.Idle)
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
                    TopBack(isHome = false, routeLabel = navScreenLabel) { navigateUp() }
                }
                //End
                Row {
                    TopSettings( navigateTo = { route-> navigateTo(route)})
                    TopNetwork(isNetworkAvailable = forgotViewModel.haveNetwork())
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
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp))
                when(authState.value) {

                    AuthState.Success -> {
                        Text(
                            text =
                            UiText.StringResource(R.string.auth_fb_resetMailSended_next, "asd").asString(),
                            style = MaterialTheme.typography.h4,
                            textDecoration = TextDecoration.None,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                        )
                        AuthButton(
                            buttonText =
                            UiText.StringResource(R.string.auth_forgotButtonBack, "asd").asString(),
                            textPadding = 30.dp,
                            buttonAction = {
                               navigateTo(Screen.AuthScreen.route)
                               forgotViewModel.setAuthState(AuthState.Idle)
                            },
                            buttonEnabled = true,
                            modifier = Modifier
                        )
                    }

                    else -> {
                        Text(
                            text = UiText.StringResource(R.string.auth_forgotForm, "asd").asString()
                                .uppercase(),
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                        )
                        Text(
                            text =
                            UiText.StringResource(R.string.auth_forgotInfo, "asd").asString(),
                            style = MaterialTheme.typography.h4,
                            textDecoration = TextDecoration.None,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                        )

                        EmailTextField(
                            authLogin = authForgotDataState.authLogin
                        ) { login -> forgotViewModel.setAuthLogin(login) }
                        Spacer(modifier = Modifier.height(8.dp))
                        AuthButton(
                            buttonText =
                            UiText.StringResource(R.string.auth_forgotButton, "asd").asString(),
                            textPadding = 30.dp,
                            buttonAction = {
                               forgotViewModel.setAuthState(AuthState.Idle)
                                forgotViewModel.sendMail()
                            },
                            buttonEnabled = forgotViewModel.isForgotButtonEnabled(),
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

        }
    }



}