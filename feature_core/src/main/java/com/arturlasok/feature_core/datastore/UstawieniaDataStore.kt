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

    val CONFIRM_TASK_STATUS = booleanPreferencesKey("confirm_task_status")

    val APP_ADDED_TO_AUTOSTART = booleanPreferencesKey("app_added_to_autostart")

    val IS_APP_OPEN = booleanPreferencesKey("is_app_open")

    val CONFIRM_TASK = intPreferencesKey("confirm_task")

    val WIDGED_STORE = stringPreferencesKey("widged_store")

    val WIDGET_INFO = booleanPreferencesKey("widget_info")
}