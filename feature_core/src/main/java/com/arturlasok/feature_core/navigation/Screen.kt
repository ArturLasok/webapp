package com.arturlasok.feature_core.navigation

import androidx.navigation.NamedNavArgument

sealed class Screen(val route:String, val arguments: List<NamedNavArgument>) {

    object StartScreen : Screen("StartScreen", emptyList())

    object SettingsScreen: Screen("SettingsScreen", emptyList())

    object AuthScreen: Screen("AuthScreen", emptyList())

}
