package com.arturlasok.feature_core.presentation.start_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.presentation.components.TopAuth
import com.arturlasok.feature_core.presentation.components.TopNetwork

import com.arturlasok.feature_core.presentation.components.TopSettings

@Composable
fun StartScreen(
    startViewModel: StartViewModel = hiltViewModel(),
    navigateTo: (route: String) -> Unit,
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
    val dataStoreDarkTheme = startViewModel.darkFromStore().collectAsState(initial = 0)
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost =  { scaffoldState.snackbarHostState },
        topBar = {
            //Back button //Top menu
            Row(modifierTopBar) {

                TopSettings( navigateTo = { route-> navigateTo(route)})
                TopAuth(navigateTo = { route ->
                    navigateTo(
                        route
                    )
                })
                TopNetwork(isNetworkAvailable = startViewModel.haveNetwork())

            }
        },
        bottomBar ={
            Text("Bottom MENU")
        }
    ) { paddingValues ->
        //content
        Box(modifier = modifierScaffold.padding(paddingValues)) {

           Column() {

               Text("Start Screen // "+"DT:"+dataStoreDarkTheme.value)
           }


        }

    }


}