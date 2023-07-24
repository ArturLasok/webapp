package com.arturlasok.webapp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.settings_screen.SettingsScreen
import com.arturlasok.feature_core.presentation.start_screen.StartScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_screen.AuthScreen

@Composable
fun NavigationComponent(
    navHostController: NavHostController,
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.StartScreen.route,

    ) {


        // Main Screen
        composable(
            route= Screen.StartScreen.route) {
            Column() {
                StartScreen(
                    navigateTo = { route-> navHostController.navigate(route)},
                    modifierTopBar = modifierTopBar,
                    modifierScaffold = modifierScaffold,
                    )

            }


        }
        // Settings Screen
        composable(
            route= Screen.SettingsScreen.route) {
            Column() {
                SettingsScreen(
                    navigateTo = { route-> navHostController.navigate(route)},
                    modifierTopBar = modifierTopBar,
                    modifierScaffold = modifierScaffold,

                )

            }



        }
        // Auth Screen
        composable(
            route= Screen.AuthScreen.route) {
            Column() {
               AuthScreen(
                   navigateTo = { route-> navHostController.navigate(route)},
                   modifierTopBar = modifierTopBar,
                   modifierScaffold = modifierScaffold,
                   )

            }



        }

    }
}