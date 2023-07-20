package com.arturlasok.webapp.feature_auth.presentation

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.arturlasok.feature_core.navigation.Screen

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    internetAvailable: Boolean,
    navHostController: NavHostController
) {
    Text("auth from vm: ${authViewModel.applications.value.packageName}"+internetAvailable+"//"+ authViewModel)
    Text("go to Home", modifier = Modifier.clickable(onClick = { navHostController.navigate(Screen.StartScreen.route)}))

}