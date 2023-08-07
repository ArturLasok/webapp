package com.arturlasok.webapp.feature_auth.presentation.auth_forgot

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
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject
@HiltViewModel
class ForgotViewModel @Inject constructor(
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
    private val authMailSendHandle = savedStateHandle.getStateFlow("authMailSendHandle",false)

    val authState = mutableStateOf(if(authMailSendHandle.value) { AuthState.Success } else { AuthState.Idle })

    val applicationContext = application

    var authForgotDataState = mutableStateOf(
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
                authForgotDataState.value =authForgotDataState.value.copy(authLogin = it)
            }.launchIn(viewModelScope)

        }
    }
    fun setAuthLogin(login: String) {
        savedStateHandle["authLogin"] = login
        authForgotDataState.value = authForgotDataState.value.copy(authLogin=login)
    }
    fun setAuthState(newState: AuthState) {
        authState.value = newState
        savedStateHandle["authMailSendHandle"] = newState == AuthState.Success
    }
    fun haveNetwork() : Boolean {
        return isOnline.isNetworkAvailable.value
    }
    fun getFireAuth() : FirebaseAuth {
        return fireAuth
    }
    private fun setMailFollowInDataStore(mail: String) {
        viewModelScope.launch {
            dataStoreInteraction.setMailFollow(mail)
        }
    }
    private fun getMailFollowFromDataStore() : Flow<String> {
        return dataStoreInteraction.getMailFollow()
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
        return isValidEmailString(authForgotDataState.value.authLogin)
    }
    fun sendMail() {

        if(ifFormIsOk()) {

            fireAuth.sendPasswordResetEmail(authForgotDataState.value.authLogin).addOnCompleteListener{task->
                if(task.isSuccessful){
                    setAuthState(AuthState.Success)
                    setMailFollowInDataStore(authForgotDataState.value.authLogin)
                } else {
                    task.exception?.let {
                        Log.i(TAG, "Email send failed with error ${it.localizedMessage}")
                        setAuthState(AuthState.AuthError(UiText.StringResource(fireBaseErrors(it.localizedMessage ?: ""),"asd").asString(application.applicationContext)))
                    }
                }
            }
        } else {
            setAuthState(AuthState.AuthError(UiText.StringResource(R.string.auth_email_form_error,"asd").asString(application.applicationContext)))
        }
    }
    fun isForgotButtonEnabled() : Boolean {
        return authState.value == AuthState.Idle

    }
}