package com.arturlasok.webapp.feature_auth.presentation.auth_login

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.webapp.feature_auth.model.AuthLoginDataState
import com.arturlasok.webapp.feature_auth.model.AuthState
import com.arturlasok.webapp.feature_auth.util.fireBaseErrors
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val dataStoreInteraction: DataStoreInteraction,
) : ViewModel() {

    private val authLogin = savedStateHandle.getStateFlow("authLogin","")
    private val authPassword = savedStateHandle.getStateFlow("authPassword","")
    private val authPasswordRepeat = savedStateHandle.getStateFlow("authPasswordRepeat","")
    private val authPasswordVisibility = savedStateHandle.getStateFlow("authPasswordVisibility",false)
    private val authRememberUser = savedStateHandle.getStateFlow("authRememberUser",false)

   val authState = mutableStateOf<AuthState>(AuthState.Idle)

    val applicationContext = application


    var authLoginDataState = mutableStateOf(
        AuthLoginDataState(
            authLogin = authLogin.value,
            authPassword = authPassword.value,
            authPasswordRepeat = authPasswordRepeat.value,
            authPasswordVisibility = authPasswordVisibility.value,
            authRememberUser = authRememberUser.value
        )
    )
    init {
        //get DataStore mail_follow
        if (authLogin.value.isEmpty()) {

                getMailFollowFromDataStore().onEach {
                    savedStateHandle["authLogin"] = it
                    authLoginDataState.value = authLoginDataState.value.copy(authLogin = it)
                }.launchIn(viewModelScope)

        }
    }
    fun setAuthLogin(login: String) {
        savedStateHandle["authLogin"] = login
        authLoginDataState.value = authLoginDataState.value.copy(authLogin=login)
    }
    fun setAuthPassword(password: String) {
        savedStateHandle["authPassword"] = password
        authLoginDataState.value = authLoginDataState.value.copy(authPassword=password)
    }
    private fun setMailFollowInDataStore(mail: String) {
        viewModelScope.launch {
            dataStoreInteraction.setMailFollow(mail)
        }
    }
    private fun getMailFollowFromDataStore() : Flow<String> {
        return dataStoreInteraction.getMailFollow()
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
    fun ifFormIsOk() : Boolean {
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        fun isValidEmailString(str: String): Boolean{
            return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
        }
        return isValidEmailString(authLoginDataState.value.authLogin) && authLoginDataState.value.authPassword.length>9
    }
    fun login() {
        if(ifFormIsOk()) {
            fireAuth.signInWithEmailAndPassword(
                authLoginDataState.value.authLogin,
                authLoginDataState.value.authPassword
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authState.value = AuthState.Success
                    setMailFollowInDataStore(authLoginDataState.value.authLogin)
                } else {
                    task.exception?.let {
                        Log.i(TAG, "Email signup failed with error ${it.localizedMessage}")
                        authState.value = AuthState.AuthError(UiText.StringResource(fireBaseErrors(it.localizedMessage ?: ""),"asd").asString(application.applicationContext))
                        setMailFollowInDataStore(authLoginDataState.value.authLogin)
                    }
                }
            }
        } else {
            authState.value = AuthState.AuthError(UiText.StringResource(R.string.auth_form_error,"asd").asString(application.applicationContext))
            setMailFollowInDataStore(authLoginDataState.value.authLogin)
        }

    }
    fun isLoginButtonEnabled() : Boolean {
        return authState.value == AuthState.Idle

    }

}