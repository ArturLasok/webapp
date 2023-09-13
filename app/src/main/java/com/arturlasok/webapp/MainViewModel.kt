package com.arturlasok.webapp

import android.app.Application
import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.webapp.feature_auth.model.AuthState
import com.arturlasok.webapp.model.UserMobileCheckState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val dataStoreInteraction: DataStoreInteraction,
    private val apiInteraction: ApiInteraction,
) : ViewModel() {


    val mainUserMobileCheckState: MutableState<UserMobileCheckState> = mutableStateOf(UserMobileCheckState.Idle)
    fun getMobileTokenStore() : Flow<String> {
        return dataStoreInteraction.getMobileToken()
    }
    fun stayOnThisDevice() {
        try {
            val sim = application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val tmr = sim.networkCountryIso
            getMobileTokenStore().take(1).onEach { dataStoreToken ->

                apiInteraction.ktor_insertOrUpdateUser(
                    token = dataStoreToken,
                    key = getFireAuth().currentUser?.uid ?: "",
                    mail = getFireAuth().currentUser?.email ?: "null",
                    simCountry = tmr
                ).onEach {response ->
                    if (response) {

                        checkMobileToken()
                        mainUserMobileCheckState.value =UserMobileCheckState.Idle

                    } else {

                        checkMobileToken()
                    }


                }.launchIn(viewModelScope).join()


            }.launchIn(viewModelScope)


        } catch (e: Exception) {

          checkMobileToken()

        }
    }
    fun checkMobileToken() {
        if (fireAuth.currentUser != null) {
            apiInteraction.ktor_checkMobileToken(
                key = fireAuth.currentUser?.uid ?: "",
                mail = fireAuth.currentUser?.email ?: ""
            ).onEach { mongoToken ->
                if (mongoToken.webUserKey?.isNotEmpty() == true) {


                    getMobileTokenStore().take(1).onEach{ dataStoreToken ->

                        // check is the same
                        if (mongoToken.webUserToken == dataStoreToken) {
                            mainUserMobileCheckState.value = UserMobileCheckState.Same
                            if (mongoToken.webUserBlocked) {
                                mainUserMobileCheckState.value = UserMobileCheckState.SameButBlocked
                            }
                        }
                        else
                        {
                            mainUserMobileCheckState.value = UserMobileCheckState.NotTheSame
                            if (mongoToken.webUserBlocked) {
                                mainUserMobileCheckState.value =
                                    UserMobileCheckState.NotTheSameAndBlocked
                            }
                        }

                    }.launchIn(viewModelScope).join()

                }

            }.launchIn(viewModelScope)

        }
    }
    fun getFireAuth() : FirebaseAuth {
        return fireAuth
    }



}