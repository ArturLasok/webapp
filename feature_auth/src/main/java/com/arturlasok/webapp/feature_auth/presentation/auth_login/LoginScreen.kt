package com.arturlasok.webapp.feature_auth.presentation.auth_login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopNetwork
import com.arturlasok.feature_core.presentation.components.TopSettings
import com.arturlasok.webapp.feature_auth.presentation.components.EmailTextField

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateTo: (route: String) -> Unit,
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
   // val authDataState = loginViewModel.authDataState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost =  { scaffoldState.snackbarHostState },
        topBar = {
            //Back button //Top menu
            Row(modifierTopBar) {
                TopBack { navigateTo(Screen.StartScreen.route) }
                TopNetwork(isNetworkAvailable = loginViewModel.haveNetwork())
                TopSettings( navigateTo = { route-> navigateTo(route)})
            }
        },
        bottomBar ={
            Text("Bottom MENU")
        }
    ) { paddingValues ->
        // data store for dark theme
        Box(modifier = modifierScaffold.padding(paddingValues)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Text(text = "ss: "+loginViewModel.getStateInfo())
                Text(
                    text = "Auth Login"
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp))
                EmailTextField(
                    authLogin = loginViewModel.getAuthLogin()
                ) { login -> loginViewModel.setAuthLogin(login) }

            }

        }
    }
}