package com.arturlasok.webapp.feature_auth.presentation.auth_reg

import android.app.Application
import android.content.Context
import android.telephony.TelephonyManager
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
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.webapp.feature_auth.model.AuthLoginDataState
import com.arturlasok.webapp.feature_auth.model.AuthState
import com.arturlasok.webapp.feature_auth.util.fireBaseErrors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class RegViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val dataStoreInteraction: DataStoreInteraction,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction
): ViewModel() {

    private val authLogin = savedStateHandle.getStateFlow("authLogin","")
    private val authPassword = savedStateHandle.getStateFlow("authPassword","")
    private val authPasswordRepeat = savedStateHandle.getStateFlow("authPasswordRepeat","")
    private val authPasswordVisibility = savedStateHandle.getStateFlow("authPasswordVisibility",false)
    private val authRememberUser = savedStateHandle.getStateFlow("authRememberUser",false)
    private val authIsPasswordTheSame = savedStateHandle.getStateFlow("authIsPasswordTheSame",true)

    val authState = mutableStateOf<AuthState>(AuthState.Idle)

    val applicationContext = application

    var authRegDataState = mutableStateOf(
        AuthLoginDataState(
            authLogin = authLogin.value,
            authPassword = authPassword.value,
            authPasswordRepeat = authPasswordRepeat.value,
            authPasswordVisibility = authPasswordVisibility.value,
            authRememberUser = authRememberUser.value,
            authIsPasswordTheSame = authIsPasswordTheSame.value
        )
    )
    fun setAuthLogin(login: String) {
        savedStateHandle["authLogin"] = login.trim()
        authRegDataState.value = authRegDataState.value.copy(authLogin=login.trim())
    }
    fun setAuthPassword(password: String) {
        savedStateHandle["authPassword"] = password.trim()
        authRegDataState.value = authRegDataState.value.copy(authPassword=password.trim())
        isPasswordsAreTheSame()
    }
    fun setAuthPasswordRepeat(passwordR: String) {
        savedStateHandle["authPasswordRepeat"] = passwordR.trim()
        authRegDataState.value = authRegDataState.value.copy(authPasswordRepeat = passwordR.trim())
        isPasswordsAreTheSame()
    }
    fun setAuthPasswordVisibility(newVal :Boolean) {
        savedStateHandle["authPasswordVisibility"] = newVal
        authRegDataState.value = authRegDataState.value.copy(authPasswordVisibility=newVal)
    }
    fun setRememberUser(newVal: Boolean) {
        savedStateHandle["authRememberUser"] = newVal
        authRegDataState.value = authRegDataState.value.copy(authRememberUser=newVal)
    }
    fun haveNetwork() : Boolean {
        return isOnline.isNetworkAvailable.value
    }
    fun getFireAuth() : FirebaseAuth {
        return fireAuth
    }
    private fun isPasswordsAreTheSame() {
        if(authRegDataState.value.authPassword.trim()==authRegDataState.value.authPasswordRepeat.trim()) {
            authRegDataState.value = authRegDataState.value.copy(authIsPasswordTheSame = true)
            savedStateHandle["authIsPasswordTheSame"] = true
        } else {
            authRegDataState.value = authRegDataState.value.copy(authIsPasswordTheSame = false)
            savedStateHandle["authIsPasswordTheSame"] = false
        }
    }
    private fun setMailFollowInDataStore(mail: String) {
        viewModelScope.launch {
            dataStoreInteraction.setMailFollow(mail)
        }
    }
    private fun setFirstLoginInDataStore(state: Boolean) {
        viewModelScope.launch {
            dataStoreInteraction.setFirstLogin(state)
        }
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
        return isValidEmailString(authRegDataState.value.authLogin)
                && authRegDataState.value.authPassword.length>9
                && authRegDataState.value.authIsPasswordTheSame
    }
    fun isRegButtonEnabled() : Boolean {
        return authState.value == AuthState.Idle

    }
    fun setAuthState(newState: AuthState) {
        authState.value = newState

    }
    fun dbSyncInsertOrUpdateUser() {
        if (getFireAuth().currentUser != null) {
            getFireAuth().currentUser?.let {




                ktorInsertOrUpdateUser(it)


            }
        } else {
            authState.value = AuthState.AuthError(
                UiText.StringResource(R.string.auth_somethingWrong, "asd")
                    .asString(applicationContext)
            )
            getFireAuth().signOut()

        }
    }
    private fun setMobileTokenInDataStore(token: String) {
        viewModelScope.launch{
            dataStoreInteraction.setMobileToken(token)
        }
    }
    private fun ktorInsertOrUpdateUser(user: FirebaseUser) {
        try {
            val sim = application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val tmr = sim.networkCountryIso
            val unixTime = System.currentTimeMillis() / 1000L
            val allowedChars = ('A'..'Z') + ('a'..'s') + ('0'..'9')
            val genTokenForThisMobile: () -> String = fun() : String {
                return (1..16)
                    .map { allowedChars.random() }
                    .joinToString("")
            }
            val token =  unixTime.toString()+"time"+genTokenForThisMobile.invoke()


        apiInteraction.ktor_insertOrUpdateUser(key = user.uid, mail = user.email ?: "null", simCountry = tmr, token = token).onEach { response->
            if(response) {
                setMobileTokenInDataStore(token)
                authState.value = AuthState.Success
            }
            else {
                getFireAuth().signOut()
                authState.value = AuthState.DbSyncError
            }
        }.launchIn(viewModelScope)
        } catch (e: Exception) {
            authState.value = AuthState.AuthError(
                UiText.StringResource(
                    R.string.auth_somethingWrong,
                    "asd"
                ).asString(applicationContext)
            )
        }

    }
    fun register() {

        if(ifFormIsOk()) {
            fireAuth.createUserWithEmailAndPassword(
                authRegDataState.value.authLogin,
                authRegDataState.value.authPassword
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setFirstLoginInDataStore(true)
                    setAuthState(AuthState.DbSync)
                    setMailFollowInDataStore(authRegDataState.value.authLogin)
                } else {
                    task.exception?.let {
                        Log.i(TAG, "Email register failed with error ${it.localizedMessage}")
                        setAuthState(AuthState.AuthError(UiText.StringResource(fireBaseErrors(it.localizedMessage ?: ""),"asd").asString(application.applicationContext)))
                        setMailFollowInDataStore(authRegDataState.value.authLogin)
                    }
                }
            }


        } else {
            setAuthState(AuthState.AuthError(UiText.StringResource(R.string.auth_form_error,"asd").asString(application.applicationContext)))

        }

    }
}