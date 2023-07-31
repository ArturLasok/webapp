package com.arturlasok.feature_core.presentation.start_screen

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.isOnline
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val dataStoreInteraction: DataStoreInteraction,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,

) : ViewModel() {
    private val test = savedStateHandle.getStateFlow("test","")
    val applications = mutableStateOf(application)

    fun darkFromStore() : Flow<Int> {
        return dataStoreInteraction.getDarkThemeInt()
    }
    fun haveNetwork() : Boolean {
        return isOnline.isNetworkAvailable.value
    }
    fun getStateInfo(): String {
        return savedStateHandle.keys().joinToString {
            it
        } + " \n" +
                "hashcodes: "+savedStateHandle.hashCode()+ " "+savedStateHandle.savedStateProvider().hashCode()
    }
    fun getFireAuth() : FirebaseAuth {
        return fireAuth
    }

}