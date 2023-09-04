package com.arturlasok.webapp.navigation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopLogOut
import com.arturlasok.feature_core.presentation.components.TopMessages
import com.arturlasok.feature_core.presentation.settings_screen.SettingsScreen
import com.arturlasok.feature_core.presentation.start_screen.StartScreen
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.webapp.feature_auth.presentation.auth_addmessage.AddMessageScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_forgot.ForgotScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_login.LoginScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_messages.MessagesScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_onemessage.OneMessagesScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_profile.ProfileScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_reg.RegScreen

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
                    isSecondScreen = false,
                    isInDualMode = false,
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

                //LANDSCAPE ORIENTATION -> Dual Screen
                if(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Log.i(TAG, "orient: land")
                    Row() {
                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {

                            ProfileScreen(
                                topBack = {
                                    TopBack(
                                        isHome = false,
                                        isSecondScreen = false,
                                        isInDualMode = true,
                                        routeLabel = UiText.StringResource(Screen.ProfileScreen.label, "asd").asString(),
                                        onBack = { navHostController.popBackStack()  })
                                    { navHostController.navigate(Screen.StartScreen.route) }
                                },
                                topEnd = { fbAuth ->
                                    //TopLogOut(navigateTo = { route ->  navHostController.navigate(route)}, firebaseAuth = fbAuth)
                                },
                                navigateTo = { route -> navHostController.navigate(route) },
                                modifierTopBar = modifierTopBar,
                                modifierScaffold = modifierScaffold,
                            )
                        }
                        Column(modifier = Modifier.fillMaxWidth()) {
                            MessagesScreen(
                                topBack = {
                                    TopBack(
                                        isHome = false,
                                        isSecondScreen = true,
                                        isInDualMode = true,
                                        routeLabel = UiText.StringResource(Screen.MessagesScreen.label, "asd").asString(),
                                        onBack = { navHostController.popBackStack()  })
                                    { navHostController.navigate(Screen.StartScreen.route) }
                                },
                                topEnd = {

                                },
                                currentRoute = it.destination.route.toString(),
                                navigateTo = { route-> navHostController.navigate(route)},
                                modifierTopBar = modifierTopBar,
                                modifierScaffold = modifierScaffold,
                                )
                        }
                    }
                }

                // PORTRAIT ORIENTATION
                else {
                    ProfileScreen(
                        topBack = {
                            TopBack(
                                isHome = false,
                                isSecondScreen = false,
                                isInDualMode = false,
                                routeLabel = UiText.StringResource(
                                    Screen.ProfileScreen.label,
                                    "asd"
                                ).asString(),
                                onBack = { navHostController.popBackStack() })
                            { navHostController.navigate(Screen.StartScreen.route) }
                        },
                        topEnd = { fbAuth ->
                            TopMessages(navigateTo = {route -> navHostController.navigate(route = route)  })
                            //TopLogOut(navigateTo = { route -> navHostController.navigate(route = route) }, firebaseAuth = fbAuth)
                        },
                        navigateTo = { route -> navHostController.navigate(route) },
                        modifierTopBar = modifierTopBar,
                        modifierScaffold = modifierScaffold,
                    )
                }
            }
        }
        // Messages List Screen
        composable(
            route= Screen.MessagesScreen.route) {
            Column() {
                navHistory(navHostController = navHostController)
                //LANDSCAPE ORIENTATION -> Dual Screen
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Log.i(TAG, "orient: land")
                    Row() {
                        Column(modifier = Modifier.fillMaxWidth(1.0f)) {

                            MessagesScreen(
                                topBack = {
                                    TopBack(
                                        isHome = false,
                                        isSecondScreen = false,
                                        isInDualMode = true,
                                        routeLabel = UiText.StringResource(
                                            Screen.MessagesScreen.label,
                                            "asd"
                                        ).asString(),
                                        onBack = { navHostController.popBackStack() })
                                    { navHostController.navigate(Screen.StartScreen.route) }
                                },
                                topEnd = {
                                },
                                currentRoute = it.destination.route.toString(),
                                navigateTo = { route -> navHostController.navigate(route) },
                                modifierTopBar = modifierTopBar,
                                modifierScaffold = modifierScaffold,
                            )
                        }
                        /*
                        Column(modifier = Modifier.fillMaxWidth()) {
                            AddMessageScreen(
                                contextId = it.arguments?.getString("contextId","") ?: "",
                                topBack = {
                                    TopBack(
                                        isHome = false,
                                        isSecondScreen = true,
                                        isInDualMode = true,
                                        routeLabel = UiText.StringResource(
                                            Screen.AddMessageScreen.label,
                                            "asd"
                                        ).asString(),
                                        onBack = { navHostController.popBackStack() })
                                    { navHostController.navigate(Screen.StartScreen.route) }
                                },
                                topEnd = {

                                },
                                navigateTo = { route -> navHostController.navigate(route) },
                                modifierTopBar = modifierTopBar,
                                modifierScaffold = modifierScaffold,
                            )
                        }

                         */
                    }
                }

                // PORTRAIT ORIENTATION
                else {
                    MessagesScreen(
                        topBack = {
                            TopBack(
                                isHome = false,
                                isSecondScreen = false,
                                isInDualMode = false,
                                routeLabel = UiText.StringResource(
                                    Screen.MessagesScreen.label,
                                    "asd"
                                ).asString(),
                                onBack = { navHostController.popBackStack() })
                            { navHostController.navigate(Screen.StartScreen.route) }
                        },
                        topEnd = {

                        },
                        currentRoute = it.destination.route.toString(),
                        navigateTo = { route -> navHostController.navigate(route) },
                        modifierTopBar = modifierTopBar,
                        modifierScaffold = modifierScaffold,
                    )

                }
            }


        }
        //Add Message Screen
        composable(
            arguments = listOf(navArgument("contextId") { defaultValue = "" }),
            route= Screen.AddMessageScreen.route) {
            Column() {
                navHistory(navHostController = navHostController)
                //LANDSCAPE ORIENTATION -> Dual Screen
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Log.i(TAG, "orient: land")
                    Row() {
                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {

                            MessagesScreen(
                                topBack = {
                                    TopBack(
                                        isHome = false,
                                        isSecondScreen = false,
                                        isInDualMode = true,
                                        routeLabel = UiText.StringResource(
                                            Screen.MessagesScreen.label,
                                            "asd"
                                        ).asString(),
                                        onBack = { navHostController.popBackStack() })
                                    { navHostController.navigate(Screen.StartScreen.route) }
                                },
                                topEnd = {
                                },
                                currentRoute = it.destination.route.toString(),
                                navigateTo = { route -> navHostController.navigate(route) },
                                modifierTopBar = modifierTopBar,
                                modifierScaffold = modifierScaffold,
                            )
                        }
                        Column(modifier = Modifier.fillMaxWidth()) {
                            AddMessageScreen(
                                contextId = it.arguments?.getString("contextId","") ?: "",
                                topBack = {
                                    TopBack(
                                        isHome = false,
                                        isSecondScreen = true,
                                        isInDualMode = true,
                                        routeLabel = UiText.StringResource(
                                            Screen.AddMessageScreen.label,
                                            "asd"
                                        ).asString(),
                                        onBack = { navHostController.popBackStack() })
                                    { navHostController.navigate(Screen.StartScreen.route) }
                                },
                                topEnd = {

                                },
                                navigateTo = { route -> navHostController.navigate(route) },
                                modifierTopBar = modifierTopBar,
                                modifierScaffold = modifierScaffold,
                            )
                        }
                    }
                }

                // PORTRAIT ORIENTATION
                else {
                    AddMessageScreen(
                        contextId = it.arguments?.getString("contextId","") ?: "",
                        topBack = {
                            TopBack(
                                isHome = false,
                                isSecondScreen = false,
                                isInDualMode = false,
                                routeLabel = UiText.StringResource(
                                    Screen.AddMessageScreen.label,
                                    "asd"
                                ).asString(),
                                onBack = { navHostController.popBackStack() })
                            { navHostController.navigate(Screen.StartScreen.route) }
                        },
                        topEnd = {

                        },
                        navigateTo = { route -> navHostController.navigate(route) },
                        modifierTopBar = modifierTopBar,
                        modifierScaffold = modifierScaffold,
                    )

                }
            }


        }
        //One Message Screen
        composable(
            route= Screen.OneMessageScreen.route,
            arguments = listOf(navArgument("mesId") { defaultValue = "-1" })
        ) {

            Column() {
                navHistory(navHostController = navHostController)
                //LANDSCAPE ORIENTATION -> Dual Screen
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Log.i(TAG, "orient: land")
                    Row() {
                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {

                            MessagesScreen(
                                topBack = {
                                    TopBack(
                                        isHome = false,
                                        isSecondScreen = false,
                                        isInDualMode = true,
                                        routeLabel = UiText.StringResource(
                                            Screen.MessagesScreen.label,
                                            "asd"
                                        ).asString(),
                                        onBack = { navHostController.popBackStack() })
                                    { navHostController.navigate(Screen.StartScreen.route) }
                                },
                                topEnd = {
                                },
                                currentRoute = it.destination.route.toString(),
                                navigateTo = { route -> navHostController.navigate(route) },
                                modifierTopBar = modifierTopBar,
                                modifierScaffold = modifierScaffold,
                            )
                        }
                        Column(modifier = Modifier.fillMaxWidth()) {
                            OneMessagesScreen(
                                topBack = {
                                    TopBack(
                                        isHome = false,
                                        isSecondScreen = true,
                                        isInDualMode = true,
                                        routeLabel = UiText.StringResource(
                                            Screen.OneMessageScreen.label,
                                            "asd"
                                        ).asString(),
                                        onBack = { navHostController.popBackStack() })
                                    { navHostController.navigate(Screen.StartScreen.route) }
                                },
                                topEnd = {

                                },
                                mesId = it.arguments?.getString("mesId","-1") ?: "-1",
                                dualScreen = true,
                                navigateTo = { route -> navHostController.navigate(route) },
                                modifierTopBar = modifierTopBar,
                                modifierScaffold = modifierScaffold,
                            )
                        }
                    }
                }

                // PORTRAIT ORIENTATION
                else {
                    OneMessagesScreen(
                        topBack = {
                            TopBack(
                                isHome = false,
                                isSecondScreen = false,
                                isInDualMode = false,
                                routeLabel = UiText.StringResource(
                                    Screen.OneMessageScreen.label,
                                    "asd"
                                ).asString(),
                                onBack = { navHostController.popBackStack() })
                            { navHostController.navigate(Screen.StartScreen.route) }
                        },
                        topEnd = {

                        },
                        mesId = it.arguments?.getString("mesId","-1") ?: "-1",
                        dualScreen = false,
                        navigateTo = { route -> navHostController.navigate(route) },
                        modifierTopBar = modifierTopBar,
                        modifierScaffold = modifierScaffold,
                    )

                }
            }


        }
    }
}