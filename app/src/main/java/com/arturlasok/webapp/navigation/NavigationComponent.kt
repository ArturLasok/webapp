package com.arturlasok.webapp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.settings_screen.SettingsScreen
import com.arturlasok.feature_core.presentation.start_screen.StartScreen
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.webapp.feature_auth.presentation.auth_login.LoginScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_reg.RegScreen

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
                    navigateUp = { navHostController.popBackStack()},
                    navScreenLabel =  UiText.StringResource(Screen.StartScreen.label,"asd").asString(),
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
                    navigateUp = { navHostController.popBackStack()},
                    navScreenLabel = UiText.StringResource(Screen.SettingsScreen.label,"asd").asString(),
                    modifierTopBar = modifierTopBar,
                    modifierScaffold = modifierScaffold,


                )

            }



        }
        // Login Screen
        composable(
            route= Screen.AuthScreen.route) {


            Column() {
               LoginScreen(
                   navigateTo = { route-> navHostController.navigate(route)},
                   navigateUp = { navHostController.popBackStack()},
                   navScreenLabel = UiText.StringResource(Screen.AuthScreen.label,"asd").asString(),
                   modifierTopBar = modifierTopBar,
                   modifierScaffold = modifierScaffold,
                   )

            }



        }
        // Login Screen
        composable(
            route= Screen.RegScreen.route) {
            Column() {
                RegScreen(
                    navigateTo = { route-> navHostController.navigate(route)},
                    navigateUp = { navHostController.popBackStack()},
                    navScreenLabel = UiText.StringResource(Screen.RegScreen.label,"asd").asString(),
                    modifierTopBar = modifierTopBar,
                    modifierScaffold = modifierScaffold,
                )

            }



        }
    }


}