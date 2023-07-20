package com.arturlasok.feature_core.presentation.start_screen

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.arturlasok.feature_core.navigation.Screen

@Composable
fun StartScreen(
    startViewModel: StartViewModel = hiltViewModel(),
    internetAvailable: Boolean,
    navHostController: NavHostController,
) {

    val dataStoreDarkTheme = startViewModel.darkFromStore().collectAsState(initial = 0)
    Text("start from vm: ${startViewModel.applications.value.packageName}"+internetAvailable)
    Text("go to Settings",
        modifier = Modifier.clickable(onClick = { navHostController.navigate(
        Screen.SettingsScreen.route)}))
    Text("go to Auth",
        modifier = Modifier.clickable(onClick = { navHostController.navigate(
            Screen.AuthScreen.route)}))

    Text("datastore dark teheme:"+dataStoreDarkTheme.value)
}