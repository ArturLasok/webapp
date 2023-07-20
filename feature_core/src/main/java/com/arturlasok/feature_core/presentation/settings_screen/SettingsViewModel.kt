package com.arturlasok.feature_core.presentation.settings_screen

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val dataStoreInteraction: DataStoreInteraction
) : ViewModel() {


    val applications = mutableStateOf(application)

    fun setDark(value:Int) {
        viewModelScope.launch{
            dataStoreInteraction.setDarkThemeInt(value)
        }

    }

    fun darkFromStore() : Flow<Int> {
        return dataStoreInteraction.getDarkThemeInt()
    }




}