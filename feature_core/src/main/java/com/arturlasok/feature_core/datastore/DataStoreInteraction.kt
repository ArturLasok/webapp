package com.arturlasok.feature_core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreInteraction @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
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
}