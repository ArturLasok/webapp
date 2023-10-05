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

    object MessagesScreen:Screen("MessagesScreen?sent={sent}&delete={delete}","MessagesScreen", emptyList(),R.string.Screen_Messages)

    object AddMessageScreen:Screen("AddMessageScreen?contextId={contextId}","AddMessageScreen", emptyList(),R.string.Screen_AddMessages)

    object OneMessageScreen:Screen("OneMessageScreen?mesId={mesId}","OneMessageScreen", emptyList(),R.string.Screen_OneMessages)

    object AddProjectScreen:Screen("AddProjectScreen","AddProjectScreen", emptyList(),R.string.Screen_AddProject)

    object DetailsScreen:Screen("DetailsScreen?openId={openId}","DetailsScreen", emptyList(),R.string.Screen_Details)

    object NewModuleScreen:Screen("NewModuleScreen","NewModuleScreen", emptyList(),R.string.Screen_NewModule)

    object EditModuleScreen:Screen("EditModuleScreen","EditModuleScreen", emptyList(),R.string.Screen_EditModule)

    object AddPageScreen:Screen("AddPageScreen","AddPageScreen", emptyList(),R.string.Screen_AddPage)

    object EditPageScreen:Screen("EditPageScreen?pageId={pageId}","EditPageScreen", emptyList(),R.string.Screen_EditPage)


}


