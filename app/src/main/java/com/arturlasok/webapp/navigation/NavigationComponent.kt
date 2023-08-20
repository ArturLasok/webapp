package com.arturlasok.webapp.navigation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.settings_screen.SettingsScreen
import com.arturlasok.feature_core.presentation.start_screen.StartScreen
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.webapp.feature_auth.presentation.auth_forgot.ForgotScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_login.LoginScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_profile.ProfileScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_reg.RegScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationComponent(
    navHostController: NavHostController,
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {
    //For test
    @Composable
    fun navHistory(navHostController: NavHostController)  {
       //Text("nav->${navHostController.backQueue.joinToString(separator = " , ") {
       //    it.destination.route.toString()
       // }} \n Last:  ${navHostController.currentBackStackEntry?.destination?.route}", style = MaterialTheme.typography.h4)
    }

    NavHost(
        navController = navHostController,
        startDestination = Screen.StartScreen.route,

    ) {
        Log.i(TAG,"Nav Recompose")

        // Main Screen
        composable(
            route= Screen.StartScreen.route,
            arguments = listOf(navArgument("logOut") { defaultValue = "logOut"},navArgument("email") { defaultValue = "email"})
        ) {

            //if arg logOut==true remove ProfileScreen from backQueue
            if(it.arguments?.getString("logOut") =="true") {
                Log.i(TAG,"Remove profile logout: YES")
                navHostController.backQueue.removeAll { nbe->
                    nbe.destination.route == "ProfileScreen"
                }
            }

            Column() {
                navHistory(navHostController = navHostController)
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
                navHistory(navHostController = navHostController)
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
                navHistory(navHostController = navHostController)
                LoginScreen(
                   navigateTo = {route->
                       //if next is Profile after login then remove AuthScreens from backQueue
                       if(route=="ProfileScreen") {

                           navHostController.backQueue.removeIf {
                               it.destination.route == "AuthScreen"
                           }
                           navHostController.backQueue.removeIf {
                               it.destination.route == "ForgotScreen"
                           }
                           navHostController.backQueue.removeIf {
                               it.destination.route == "RegScreen"
                           }
                           navHostController.navigate(route)
                           {
                               popUpTo(Screen.AuthScreen.route) { inclusive = true }
                           }

                       } else {
                           navHostController.navigate(route)
                       }

                                },
                   navigateUp = { navHostController.popBackStack()},
                   navScreenLabel = UiText.StringResource(Screen.AuthScreen.label,"asd").asString(),
                   modifierTopBar = modifierTopBar,
                   modifierScaffold = modifierScaffold,
                   )

            }



        }
        // RegisterScreen
        composable(
            route= Screen.RegScreen.route) {
            Column() {
                navHistory(navHostController = navHostController)
                RegScreen(
                    navigateTo = { route->

                        //if next is AuthScreen after RegScreen then remove RegScreen from backQueue
                        if(route=="AuthScreen") {
                            navHostController.backQueue.removeIf {
                                it.destination.route == "RegScreen"
                            }
                            navHostController.navigate(route) { popUpTo(Screen.RegScreen.route){ inclusive = true }  }
                        } else {
                            navHostController.navigate(route)
                        }


                    },
                    navigateUp = { navHostController.popBackStack()},
                    navScreenLabel = UiText.StringResource(Screen.RegScreen.label,"asd").asString(),
                    modifierTopBar = modifierTopBar,
                    modifierScaffold = modifierScaffold,
                )

            }



        }
        // ForgotScreen
        composable(
            route= Screen.ForgotScreen.route) {
            Column() {
                navHistory(navHostController = navHostController)
                ForgotScreen(
                    navigateTo = { route-> navHostController.navigate(route)},
                    navigateUp = { navHostController.popBackStack()},
                    navScreenLabel = UiText.StringResource(Screen.ForgotScreen.label,"asd").asString(),
                    modifierTopBar = modifierTopBar,
                    modifierScaffold = modifierScaffold,
                )

            }



        }
        // ProfileScreen
        composable(
            route= Screen.ProfileScreen.route) {
            Column() {
                navHistory(navHostController = navHostController)
                ProfileScreen(
                    navigateTo = { route-> navHostController.navigate(route) },
                    navigateUp = { navHostController.popBackStack()},
                    navScreenLabel = UiText.StringResource(Screen.ProfileScreen.label,"asd").asString(),
                    modifierTopBar = modifierTopBar,
                    modifierScaffold = modifierScaffold,
                )

            }



        }
    }
  


}