package com.arturlasok.webapp.feature_auth.presentation.auth_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopNetwork

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    navigateTo: (route: String) -> Unit,
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost =  { scaffoldState.snackbarHostState },
        topBar = {
            //Back button //Top menu
            Row(modifierTopBar) {

                TopBack { navigateTo(Screen.StartScreen.route) }
                TopNetwork(isNetworkAvailable = authViewModel.haveNetwork())

            }
        },
        bottomBar ={
            Text("Bottom MENU")
        }
    ) { paddingValues ->
        // data store for dark theme
        Box(modifier = modifierScaffold.padding(paddingValues)) {
            Column() {
                Text(
                    text = "Auth Screen"
                )

            }

        }
    }
}