package com.arturlasok.webapp.feature_auth.presentation.auth_login

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.webapp.feature_auth.model.AuthLoginDataState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
) : ViewModel() {

    private val authLogin = savedStateHandle.getStateFlow("authLogin","")
    private val authPassword = savedStateHandle.getStateFlow("authPassword","")
    private val authPasswordRepeat = savedStateHandle.getStateFlow("authPasswordRepeat","")
    private val authPasswordVisibility = savedStateHandle.getStateFlow("authPasswordVisibility",false)
    private val authRememberUser = savedStateHandle.getStateFlow("authRememberUser",false)

    var authLoginDataState = mutableStateOf(
        AuthLoginDataState(
            authLogin = authLogin.value,
            authPassword = authPassword.value,
            authPasswordRepeat = authPasswordRepeat.value,
            authPasswordVisibility = authPasswordVisibility.value,
            authRememberUser = authRememberUser.value
        )
    )

    fun setAuthLogin(login: String) {
        savedStateHandle["authLogin"] = login
        authLoginDataState.value = authLoginDataState.value.copy(authLogin=login)
    }
    fun setAuthPassword(password: String) {
        savedStateHandle["authPassword"] = password
        authLoginDataState.value = authLoginDataState.value.copy(authPassword=password)
    }
    fun setAuthPasswordRepeat(passwordR: String) {
        savedStateHandle["authPasswordRepeat"] = passwordR
    }
    fun setAuthPasswordVisibility(newVal :Boolean) {
        savedStateHandle["authPasswordVisibility"] = newVal
        authLoginDataState.value = authLoginDataState.value.copy(authPasswordVisibility=newVal)
    }
    fun setRememberUser(newVal: Boolean) {
        savedStateHandle["authRememberUser"] = newVal
        authLoginDataState.value = authLoginDataState.value.copy(authRememberUser=newVal)
    }
    fun haveNetwork() : Boolean {
        return isOnline.isNetworkAvailable.value
    }
    fun getFireAuth() : FirebaseAuth {
        return fireAuth
    }

}