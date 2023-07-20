package com.arturlasok.webapp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.settings_screen.SettingsScreen
import com.arturlasok.feature_core.presentation.start_screen.StartScreen
import com.arturlasok.webapp.feature_auth.presentation.AuthScreen

@Composable
fun NavigationComponent(navHostController: NavHostController,internetAvailable: Boolean) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.StartScreen.route
    ) {


        // Main Screen
        composable(
            route= Screen.StartScreen.route) {
            Column {
                StartScreen(internetAvailable = internetAvailable, navHostController = navHostController)

            }


        }
        // Main Screen
        composable(
            route= Screen.SettingsScreen.route) {
            Column() {
                SettingsScreen(internetAvailable = internetAvailable, navHostController = navHostController)

            }



        }
        // Main Screen
        composable(
            route= Screen.AuthScreen.route) {
            Column() {
               AuthScreen(
                    internetAvailable = internetAvailable, navHostController = navHostController
                )

            }



        }

    }
}