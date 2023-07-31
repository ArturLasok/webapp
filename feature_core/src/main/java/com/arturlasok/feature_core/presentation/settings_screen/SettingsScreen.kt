package com.arturlasok.feature_core.presentation.settings_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopNetwork

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navigateTo: (route: String) -> Unit,
    navigateUp:()->Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
    val dataStoreDarkTheme = settingsViewModel.darkFromStore().collectAsState(initial = 0)
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
                    TopNetwork(isNetworkAvailable = settingsViewModel.haveNetwork())
                }

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
                    color = MaterialTheme.colors.onBackground,
                    text = "datastore dark theme:" + dataStoreDarkTheme.value
                )
                Text(
                    color = MaterialTheme.colors.onBackground, text = "set other dark",
                    modifier = Modifier.clickable(onClick = {

                        settingsViewModel.setDark(if (dataStoreDarkTheme.value > 0) 0 else 2)
                    })
                )
            }

        }
    }
}