package com.arturlasok.webapp.feature_auth.presentation.auth_login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import com.arturlasok.webapp.feature_auth.presentation.components.PasswordTextField
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateTo: (route: String) -> Unit,
    navigateUp:()->Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {

    val snackbarController = SnackbarController(rememberCoroutineScope())
    val authLoginDataState = loginViewModel.authLoginDataState.value
    val authState = loginViewModel.authState
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = authState.value, block = {
        if(authState.value == AuthState.DbSync) {
            loginViewModel.dbSyncInsertOrUpdateUser()
        }
        if(authState.value==AuthState.Success) {   navigateTo(Screen.ProfileScreen.route)  }
        if(authState.value is AuthState.AuthError) {
            snackMessage(
                snackType = SnackType.ERROR,
                message = (authState.value as AuthState.AuthError).message ?: UiText.StringResource(R.string.auth_somethingWrong,"asd").asString(loginViewModel.applicationContext.applicationContext),
                actionLabel = UiText.StringResource(R.string.auth_ok,"asd").asString(loginViewModel.applicationContext.applicationContext),
                snackbarController =snackbarController,
                scaffoldState =scaffoldState)
            delay(2000)
            authState.value = AuthState.Idle
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
                    TopBack(isHome = false, isSecondScreen = false, isInDualMode = false, routeLabel = navScreenLabel, onBack = { navigateUp() })
                    { navigateTo(Screen.StartScreen.route) }
                }
                //End
                Row {
                    TopSettings( navigateTo = { route-> navigateTo(route)})
                    TopNetwork(isNetworkAvailable = loginViewModel.haveNetwork())
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
                    Surface(shape = MaterialTheme.shapes.medium, elevation = 20.dp, color = MaterialTheme.colors.background, modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp).padding(top = 0.dp)) {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    )
                    Text(
                        fontWeight = FontWeight.Bold,
                        text = UiText.StringResource(R.string.auth_loginForm, "asd").asString()
                            .uppercase(),
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    Text(
                        text = UiText.StringResource(R.string.auth_register, "asd").asString(),
                        style = MaterialTheme.typography.h3,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable(onClick = { navigateTo(Screen.RegScreen.route) })
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    EmailTextField(
                        authLogin = authLoginDataState.authLogin
                    ) { login -> loginViewModel.setAuthLogin(login) }
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordTextField(
                        authPassword = authLoginDataState.authPassword,
                        setAuthPassword = { password -> loginViewModel.setAuthPassword(password) },
                        passwordVisibility = authLoginDataState.authPasswordVisibility,
                        isPasswordTheSame = true,
                        isPasswordRepeatField = false,
                        setPasswordVisibility = { newVal ->
                            loginViewModel.setAuthPasswordVisibility(
                                newVal
                            )
                        }
                    )
                    /*
                Spacer(modifier = Modifier.height(4.dp))
                AuthCheckBox(
                    modifier = Modifier,
                    textStyling = MaterialTheme.typography.h3,
                    checkQuestion = UiText.StringResource(R.string.auth_rememberQuestion,"asd").asString(),
                    checkValue = authLoginDataState.authRememberUser,
                    setNewCheckValue = { newVal ->  loginViewModel.setRememberUser(newVal)}
                )

                 */
                    Spacer(modifier = Modifier.height(8.dp))
                    AuthButton(
                        buttonText = UiText.StringResource(R.string.auth_loginButton, "asd")
                            .asString(),
                        textPadding = 80.dp,
                        buttonAction = {
                            authState.value = AuthState.Interact
                            loginViewModel.login()
                        },
                        buttonEnabled = loginViewModel.isLoginButtonEnabled(),
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text(
                            text = UiText.StringResource(R.string.auth_registerShort, "asd")
                                .asString(),
                            style = MaterialTheme.typography.h3,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable(onClick = { navigateTo(Screen.RegScreen.route) })
                        )
                        //Spacer(modifier = Modifier.width(10.dp))
                        Text("   ")
                        Text(
                            text = UiText.StringResource(R.string.auth_dontRememberPassword, "asd")
                                .asString(),
                            style = MaterialTheme.typography.h3,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable(onClick = { navigateTo(Screen.ForgotScreen.route) })
                        )

                    }
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp)
                            )
                    }
                }
            }
        }
    }
}