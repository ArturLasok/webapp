package com.arturlasok.feature_core.navigation

import androidx.navigation.NamedNavArgument
import com.arturlasok.feature_core.R


sealed class  Screen(val route:String, val arguments: List<NamedNavArgument>, val label: Int) {

    object StartScreen : Screen("StartScreen", emptyList(), R.string.Screen_Start)

    object SettingsScreen: Screen("SettingsScreen", emptyList(),R.string.Screen_Settings)

    object AuthScreen: Screen("AuthScreen", emptyList(),R.string.Screen_Auth)

    object RegScreen:Screen("RegScreen", emptyList(),R.string.Screen_Reg)

}


