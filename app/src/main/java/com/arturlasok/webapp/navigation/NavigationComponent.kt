package com.arturlasok.webapp.navigation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopMessages
import com.arturlasok.feature_core.presentation.settings_screen.SettingsScreen
import com.arturlasok.feature_core.presentation.start_screen.StartScreen
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.presentation.creator_addproject.AddProjectScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_addmessage.AddMessageScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_forgot.ForgotScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_login.LoginScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_messages.MessagesScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_onemessage.OneMessagesScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_profile.ProfileScreen
import com.arturlasok.webapp.feature_auth.presentation.auth_reg.RegScreen
import com.arturlasok.webapp.model.UserMobileCheckState
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationComponent(
    checkMobileToken:() -> Unit,
    mobileCheckState: UserMobileCheckState,
    changeStateMobileCheckState:(newState: UserMobileCheckState) -> Unit,
    setUserStayOnThisDevice:() -> Unit,
    navHostController: NavHostController,
    modifierTopBar: Modifier,
    modifierScaffold: Modifier,
    firebaseAuth: FirebaseAuth,
) {
    //For all routes
    @Composable
    fun navTasks(navHostController: NavHostController)  {
        //reaction to device selection by user
        LaunchedEffect(key1 = mobileCheckState, block = {
            when(mobileCheckState) {
                is UserMobileCheckState.Stay -> {
                    setUserStayOnThisDevice()
                    //changeStateMobileCheckState(UserMobileCheckState.Idle)
                }
                is UserMobileCheckState.LogOut -> {
                    firebaseAuth.signOut();
                    changeStateMobileCheckState(UserMobileCheckState.Idle)
                    navHostController.navigate(Screen.StartScreen.routeWithArgs+"?logOut=true&email=none")
                }
                else -> {
                   //nothing
                }
            }

        })
        //NAV Queue
        //Text("nav->${navHostController.backQueue.joinToString(separator = " , ") {
        //   it.destination.route.toString()
        //}} \n Last:  ${navHostController.currentBackStackEntry?.destination?.route}", style = MaterialTheme.typography.h4)
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
                    nbe.destination.route?.contains("StartScreen") == false
                }
                navHostController.currentBackStackEntry?.arguments?.clear()
            }

            Column() {
                navTasks(navHostController = navHostController)
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
                navTasks(navHostController = navHostController)
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
                navTasks(navHostController = navHostController)
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
                navTasks(navHostController = navHostController)
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
                navTasks(navHostController = navHostController)
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
                navTasks(navHostController = navHostController)
                val notViewed = remember {
                    mutableStateOf(0)
                }
                LaunchedEffect(key1 = true, block = {
                    checkMobileToken()
                })
                //LANDSCAPE ORIENTATION -> Dual Screen
                if(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Log.i(TAG, "orient: land")
                    Row() {
                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {

                            ProfileScreen(
                                notViewed = { },
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
                                isDualMode = false,
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
                        notViewed = { number -> notViewed.value = number },
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
                            TopMessages(navigateTo = {route -> navHostController.navigate(route = route)  },notViewed.value)
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
            arguments = listOf(navArgument("sent") { defaultValue = false },navArgument("delete") { defaultValue = "" }),
            route= Screen.MessagesScreen.route) {
            val sent = it.arguments?.getBoolean("sent",false) ?: false
            if((it.arguments?.getBoolean("sent") == true)
            ) {
                Log.i(
                    TAG,
                    "REMOVE BECOSE SENT! ${navHostController.backQueue.last().destination.route}"
                )
                navHostController.backQueue.removeAll { nbe ->
                    nbe.destination.route?.contains("AddMessageScreen") ?: false

                }
                navHostController.currentBackStackEntry?.arguments?.clear()
            }
            val delete = it.arguments?.getString("delete","") ?: ""
            Log.i(TAG, "DELETE NAV ARG VAL: ${delete}")
            LaunchedEffect(key1 = true, block = {
                checkMobileToken()
            })
            Column() {
                navTasks(navHostController = navHostController)
                    MessagesScreen(
                        delete = delete,
                        sent = sent,
                        isDualMode = false,
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
        //Add Message Screen
        composable(
            arguments = listOf(navArgument("contextId") { defaultValue = "" }),
            route= Screen.AddMessageScreen.route) {
            Column() {
                navTasks(navHostController = navHostController)
                LaunchedEffect(key1 = true, block = {
                    checkMobileToken()
                })
                //LANDSCAPE ORIENTATION -> Dual Screen
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Log.i(TAG, "orient: land")
                    Row() {
                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {

                            MessagesScreen(
                                isDualMode = true,
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
                                navigateTo = { route -> navHostController.navigate(route)},
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
            LaunchedEffect(key1 = true, block = {
                checkMobileToken()
            })
            Column() {
                navTasks(navHostController = navHostController)
                //LANDSCAPE ORIENTATION -> Dual Screen
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Log.i(TAG, "orient: land")
                    Row() {
                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {

                            MessagesScreen(
                                isDualMode = true,
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
        // Add Project Screen
        composable(
            route= Screen.AddProjectScreen.route) {
            Column() {
                navTasks(navHostController = navHostController)
                    AddProjectScreen(
                    isSecondScreen = false,
                    isInDualMode = false,
                    navigateTo = { route-> navHostController.navigate(route)},
                    navigateUp = { navHostController.popBackStack()},
                    navScreenLabel = UiText.StringResource(Screen.AddProjectScreen.label,"asd").asString(),
                    modifierTopBar = modifierTopBar,
                    modifierScaffold = modifierScaffold,
                    )

            }
        }
    }
}