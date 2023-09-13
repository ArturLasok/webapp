package com.arturlasok.feature_core.presentation.settings_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopNetwork
import com.arturlasok.feature_core.util.SnackType
import com.arturlasok.feature_core.util.SnackbarController
import com.arturlasok.feature_core.util.snackMessage

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    isSecondScreen: Boolean,
    isInDualMode: Boolean,
    navigateTo: (route: String) -> Unit,
    navigateUp:()->Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
    //snackbar controller
    val snackbarController = SnackbarController(rememberCoroutineScope())

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
                    TopBack(isHome = false, isSecondScreen = isSecondScreen,isInDualMode = isInDualMode, routeLabel = navScreenLabel, onBack = { navigateUp() })
                    { navigateTo(Screen.StartScreen.route) }
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
            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() },
                modifier = Modifier
                    .zIndex(1.0f)
                    .padding(
                        top = 1.dp
                    )
            )
            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 20.dp,
                color = MaterialTheme.colors.background,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                    .padding(top = 0.dp)
            ) {
            Column(modifier = Modifier.fillMaxSize()) {

                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            color = MaterialTheme.colors.onBackground,
                            text = "datastore dark theme:" + dataStoreDarkTheme.value
                        )
                        Text(
                            color = MaterialTheme.colors.onBackground, text = "set other dark",
                            modifier = Modifier.clickable(onClick = {

                                settingsViewModel.setDark(if (dataStoreDarkTheme.value > 0) 0 else 2)
                                snackMessage(
                                    snackType = SnackType.NORMAL,
                                    message = "Theme has change",
                                    actionLabel = "OK",
                                    snackbarController = snackbarController,
                                    scaffoldState = scaffoldState
                                )

                            })
                        )
                    }
                }
            }

        }
    }
}