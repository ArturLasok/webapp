package com.arturlasok.webapp.feature_auth.presentation.auth_login

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.arturlasok.feature_core.util.SavedStateHandlerInteraction
import com.arturlasok.feature_core.util.isOnline
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    //private val savedStateHandlerInteraction: SavedStateHandlerInteraction

) : ViewModel() {

    private val authLogin = savedStateHandle.getStateFlow("authLogin","")
    private val authPassword = savedStateHandle.getStateFlow("authPassword","")
    private val authPasswordRepeat = savedStateHandle.getStateFlow("authPasswordRepeat","")
    //private val authLogin = savedStateHandlerInteraction.getSavedState().getStateFlow("authLogin","")
    //private val authPassword = savedStateHandlerInteraction.getSavedState().getStateFlow("authPassword","")
    //private val authPasswordRepeat = savedStateHandlerInteraction.getSavedState().getStateFlow("authPasswordRepeat","")

    fun getStateInfo(): String {
        return savedStateHandle.keys().joinToString {
            it
        } + " \nhashcodes: "+savedStateHandle.hashCode()+ " "+savedStateHandle.savedStateProvider().hashCode()
    }
    fun setAuthLogin(login: String) {
        //savedStateHandlerInteraction.getSavedState()["authLogin"] = login
        savedStateHandle["authLogin"] = login
    }
    fun getAuthLogin() : String {
        return authLogin.value
    }

    val applications = mutableStateOf(application)
    fun haveNetwork() : Boolean {
        return isOnline.isNetworkAvailable.value
    }
    fun getFireAuth() : FirebaseAuth {
        return fireAuth
    }

}