package com.arturlasok.feature_creator.presentation.creator_newmodule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.DefaultSnackbar
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopSettings
import com.arturlasok.feature_core.util.SnackbarController
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewModuleScreen(
    newModuleViewModel: NewModuleViewModel = hiltViewModel(),
    isSecondScreen: Boolean,
    isInDualMode: Boolean,
    navigateTo: (route: String) -> Unit,
    navigateUp: () -> Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier,
) {
    val dataStoreDarkTheme = newModuleViewModel.darkFromStore().collectAsState(initial = 0)
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val scaffoldState = rememberScaffoldState()
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(

        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        topBar = {
            //Back button //Top menu
            Row(
                modifierTopBar,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {

                //Front
                Row {
                    TopBack(
                        isHome = false,
                        isSecondScreen = false,
                        isInDualMode = false,
                        routeLabel = navScreenLabel,
                        onBack = { navigateUp() })
                    { navigateTo(Screen.StartScreen.route) }
                }

                //End
                Row {
                    TopSettings(navigateTo = { route -> navigateTo(route) })
                    //TopNetwork(isNetworkAvailable = startViewModel.haveNetwork())
                }


            }
        },
        bottomBar = {

        },

        ) { paddingValues ->


        Column(
            modifier = modifierScaffold
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() },
                modifier = Modifier
                    .zIndex(1.0f)
                    .padding(
                        top = 1.dp
                    )
            )
            val animm = remember { mutableStateOf(0) }
            LaunchedEffect(key1 = true) {
                delay(100)
                animm.value = 1
            }

            AnimatedVisibility(
                visible = animm.value == 1,
                exit = fadeOut(
                    animationSpec = tween(delayMillis = 500)
                ),
                enter = fadeIn(
                    animationSpec = tween(
                        delayMillis = 500,
                        easing = FastOutSlowInEasing,
                        durationMillis = 600
                    )
                )
            ) {
            }
        }
    }
}