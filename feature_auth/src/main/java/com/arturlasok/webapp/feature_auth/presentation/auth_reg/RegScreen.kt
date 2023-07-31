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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopNetwork
import com.arturlasok.feature_core.presentation.components.TopSettings
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.webapp.feature_auth.presentation.auth_login.LoginViewModel
import com.arturlasok.webapp.feature_auth.presentation.components.AuthButton
import com.arturlasok.webapp.feature_auth.presentation.components.EmailTextField
import com.arturlasok.webapp.feature_auth.presentation.components.PasswordTextField

@Composable
fun RegScreen(
    regViewModel: LoginViewModel = hiltViewModel(),
    navigateTo: (route: String) -> Unit,
    navigateUp:()->Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
   val authRegDataState = regViewModel.authLoginDataState.value

    val scaffoldState = rememberScaffoldState()
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
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Auth Reg"
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
                    setPasswordVisibility = {newVal ->  regViewModel.setAuthPasswordVisibility(newVal)}
                )
                Spacer(modifier = Modifier.height(4.dp))
                PasswordTextField(
                    authPassword = authRegDataState.authPasswordRepeat,
                    setAuthPassword = { password -> regViewModel.setAuthPasswordRepeat(password) },
                    passwordVisibility = authRegDataState.authPasswordVisibility,
                    setPasswordVisibility = {newVal ->  regViewModel.setAuthPasswordVisibility(newVal)}
                )
                Spacer(modifier = Modifier.height(8.dp))
                AuthButton(
                    buttonText = UiText.StringResource(R.string.auth_signupButton,"asd").asString(),
                    textPadding = 50.dp,
                    buttonAction = {},
                    buttonEnabled = true,
                    modifier = Modifier)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text= UiText.StringResource(R.string.auth_policy,"asd").asString(),
                    style = MaterialTheme.typography.h5)

            }
        }
    }


}