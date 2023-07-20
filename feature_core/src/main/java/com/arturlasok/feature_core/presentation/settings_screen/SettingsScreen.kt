package com.arturlasok.feature_core.presentation.settings_screen

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.arturlasok.feature_core.navigation.Screen

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    internetAvailable: Boolean,
    navHostController: NavHostController,
) {
    // data store for dark theme
    val dataStoreDarkTheme = settingsViewModel.darkFromStore().collectAsState(initial = 0)
    Text("ss from vm: ${settingsViewModel.applications.value.packageName}"+internetAvailable)
    Text("go to Home", modifier = Modifier.clickable(onClick = { navHostController.navigate(Screen.StartScreen.route)}))
    Text("datastore dark theme:"+dataStoreDarkTheme.value)
    Text("set dark",
        modifier = Modifier.clickable(onClick = {

            settingsViewModel.setDark(if(dataStoreDarkTheme.value>0) 0 else 1)
        }))
}