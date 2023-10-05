package com.arturlasok.feature_core.datastore

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UstawieniaDataStore @Inject constructor(app: Application) {

    val DARK_OPT = intPreferencesKey("dark_theme_on")

    val MAIL_FOLLLOW = stringPreferencesKey("mail_follow")

    val FIRST_LOGIN = booleanPreferencesKey("first_login")

    val SELECTED_MESSAGE = stringPreferencesKey("selected_message")

    val MOBILE_TOKEN = stringPreferencesKey("mobile_token")

    val TEMPORARY_PROJECTS_TOKEN = stringPreferencesKey("temporary_project_token")

    val OPEN_PROJECT = stringPreferencesKey("open_project")


}