package com.arturlasok.webapp.feature_auth.presentation.auth_reg

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
fun RegScreen(
    regViewModel: RegViewModel = hiltViewModel(),
    navigateTo: (route: String) -> Unit,
    navigateUp:()->Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
    //TODO
    //Go to profile after login with nav parameter inclusive true! because in other case you not be able to back to home screen
    //
    //snackbar controller
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val authRegDataState = regViewModel.authRegDataState.value
    val authState = regViewModel.authState
    val scaffoldState = rememberScaffoldState()


    LaunchedEffect(key1 = authState.value, block = {
        if(authState.value== AuthState.Success) {   navigateTo(Screen.ProfileScreen.route)  }
        if(authState.value is AuthState.AuthError) {
            snackMessage(
                snackType = SnackType.ERROR,
                message = (authState.value as AuthState.AuthError).message ?: UiText.StringResource(R.string.auth_somethingWrong,"asd").asString(regViewModel.applicationContext.applicationContext),
                actionLabel = "OK",
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
                    TopBack(isHome = false, routeLabel = navScreenLabel) { navigateUp() }
                }
                //End
                Row {
                    TopSettings( navigateTo = { route-> navigateTo(route)})
                    TopNetwork(isNetworkAvailable = regViewModel.haveNetwork())
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

                Text(
                    text= UiText.StringResource(R.string.auth_signupForm,"asd").asString().uppercase(),
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp))
                EmailTextField(
                    authLogin = authRegDataState.authLogin
                ) { login -> regViewModel.setAuthLogin(login) }
                Spacer(modifier = Modifier.height(8.dp))
                PasswordTextField(
                    authPassword = authRegDataState.authPassword,
                    setAuthPassword = { password -> regViewModel.setAuthPassword(password) },
                    passwordVisibility = authRegDataState.authPasswordVisibility,
                    isPasswordTheSame = authRegDataState.authIsPasswordTheSame,
                    isPasswordRepeatField = false,
                    setPasswordVisibility = {newVal ->  regViewModel.setAuthPasswordVisibility(newVal)}
                )
                Spacer(modifier = Modifier.height(4.dp))
                PasswordTextField(
                    authPassword = authRegDataState.authPasswordRepeat,
                    setAuthPassword = { password -> regViewModel.setAuthPasswordRepeat(password) },
                    passwordVisibility = authRegDataState.authPasswordVisibility,
                    isPasswordTheSame = authRegDataState.authIsPasswordTheSame,
                    isPasswordRepeatField = true,
                    setPasswordVisibility = {newVal ->  regViewModel.setAuthPasswordVisibility(newVal)}
                )
                Spacer(modifier = Modifier.height(2.dp))
                if(authRegDataState.authIsPasswordTheSame) {
                    Text(
                        text = " ",
                        style = MaterialTheme.typography.h3
                    )
                } else {
                    Text(
                        text = UiText.StringResource(R.string.auth_notSamePasswords, "asd").asString(),
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.error
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                AuthButton(
                    buttonText = UiText.StringResource(R.string.auth_signupButton,"asd").asString(),
                    textPadding = 80.dp,
                    buttonAction = {
                        //authState.value = AuthState.Idle
                        regViewModel.register()
                                   },
                    buttonEnabled = regViewModel.isRegButtonEnabled(),
                    modifier = Modifier)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text= UiText.StringResource(R.string.auth_policy,"asd").asString(),
                    style = MaterialTheme.typography.h5)

            }
        }
    }



}
