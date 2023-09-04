package com.arturlasok.feature_core.navigation

import androidx.navigation.NamedNavArgument
import com.arturlasok.feature_core.R


sealed class  Screen(val route:String, val routeWithArgs:String,val arguments: List<NamedNavArgument>, val label: Int) {

    object StartScreen : Screen("StartScreen?logOut={logOut}&email={email}","StartScreen", emptyList(), R.string.Screen_Start)

    object SettingsScreen: Screen("SettingsScreen", "SettingsScreen", emptyList(),R.string.Screen_Settings)

    object AuthScreen: Screen("AuthScreen","AuthScreen", emptyList(),R.string.Screen_Auth)

    object RegScreen:Screen("RegScreen","AuthScreen",emptyList(),R.string.Screen_Reg)

    object ForgotScreen:Screen("ForgotScreen","ForgotScreen", emptyList(),R.string.Screen_Forgot)

    object ProfileScreen:Screen("ProfileScreen","ProfileScreen", emptyList(),R.string.Screen_Profile)

    object MessagesScreen:Screen("MessagesScreen","MessagesScreen", emptyList(),R.string.Screen_Messages)

    object AddMessageScreen:Screen("AddMessageScreen?contextId={contextId}","AddMessageScreen", emptyList(),R.string.Screen_AddMessages)

    object OneMessageScreen:Screen("OneMessageScreen?mesId={mesId}","OneMessageScreen", emptyList(),R.string.Screen_OneMessages)


}


