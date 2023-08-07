package com.arturlasok.feature_core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreInteraction @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    //Dark theme
    fun getDarkThemeInt() : Flow<Int> {
        //Data Store dark theme // 0 - default, 1 - light, 2 - dark
        val IS_DARK_THEME = intPreferencesKey("dark_theme_on")
        val dataFromStore : Flow<Int> =  dataStore.data.map { pref->
            pref[IS_DARK_THEME] ?: 0
        }

        return dataFromStore
    }
    suspend fun setDarkThemeInt(value: Int) {
        val IS_DARK_THEME = intPreferencesKey("dark_theme_on")
        dataStore.edit { pref ->
            pref[IS_DARK_THEME] = value
        }
    }
    //Mail follow
    fun getMailFollow() : Flow<String> {
        //Data Store mail follow // "" - default
        val MAIL_FOLLOW = stringPreferencesKey("mail_follow")
        val dataFromStore : Flow<String> =  dataStore.data.map { pref->
            pref[MAIL_FOLLOW] ?: ""
        }

        return dataFromStore
    }
    suspend fun setMailFollow(value: String) {
        val MAIL_FOLLOW = stringPreferencesKey("mail_follow")
        dataStore.edit { pref ->
            pref[MAIL_FOLLOW] = value
        }
    }
    //first login
    fun getFirstLogin() : Flow<Boolean> {
        //Data Store mail follow // "" - default
        val FIRST_LOGIN = booleanPreferencesKey("first_login")
        val dataFromStore : Flow<Boolean> =  dataStore.data.map { pref->
            pref[FIRST_LOGIN] ?: false
        }

        return dataFromStore
    }
    suspend fun setFirstLogin(value: Boolean) {
        val FIRST_LOGIN = booleanPreferencesKey("first_login")
        dataStore.edit { pref ->
            pref[FIRST_LOGIN] = value
        }
    }
}