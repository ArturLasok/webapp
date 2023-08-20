package com.arturlasok.webapp.feature_auth.presentation.auth_profile

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.webapp.feature_auth.data.repository.ApiInteraction
import com.arturlasok.webapp.feature_auth.data.repository.RoomInteraction
import com.arturlasok.webapp.feature_auth.model.ProfileDataState
import com.arturlasok.webapp.feature_auth.model.ProfileInteractionState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val dataStoreInteraction: DataStoreInteraction,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction
    ):ViewModel() {


    val verificationMailButtonEnabled = mutableStateOf(true)
    val verificationMailButtonVisible = mutableStateOf(true)
    val verificationCheckButtonEnabled = mutableStateOf(true)
    val serverTime = mutableStateOf("")

    private val profileMail = savedStateHandle.getStateFlow("profileMail","")
    private val profileVerified = savedStateHandle.getStateFlow("profileVerified",false)
    private val profileFirstLogin = savedStateHandle.getStateFlow("profileFirstLogin",true)

    val applicationContext = application

    val profileDataState = mutableStateOf(
        ProfileDataState(
            profileMail = profileMail.value,
            profileVerified = profileVerified.value,
            profileFirstLogin = profileFirstLogin.value,
        )
    )

    val profileInteractionState = mutableStateOf<ProfileInteractionState>(ProfileInteractionState.Idle)


    init {

        //fist login check in DataStore
        getFirstLoginFromDataStore().onEach { first->
            profileDataState.value = profileDataState.value.copy(profileFirstLogin=first)
            if(first)  { setFirstLoginInDataStore(false) }
        }.launchIn(viewModelScope)
        //set user mail
        profileDataState.value = profileDataState.value.copy(profileMail =  getUserMail())
        //set is verified user
        profileDataState.value = profileDataState.value.copy(profileVerified = getIsVerified())


    }
    fun getServerTime() {

            apiInteraction.getServerTime().onEach {

                serverTime.value = it

            }.launchIn(viewModelScope)

    }

    private fun getUserMail() : String {
        return getFireAuth().currentUser?.email ?: "empty"
    }
    fun getIsVerified() : Boolean {
        return getFireAuth().currentUser?.isEmailVerified ?: false
    }


    fun haveNetwork() : Boolean {
        return isOnline.isNetworkAvailable.value
    }
    fun getFireAuth() : FirebaseAuth {
        return fireAuth
    }
    private fun getMailFollowFromDataStore() : Flow<String> {
        return dataStoreInteraction.getMailFollow()
    }
    private fun getFirstLoginFromDataStore() : Flow<Boolean> {
        //return flowOf(true)
        return dataStoreInteraction.getFirstLogin()
    }
    private fun setFirstLoginInDataStore(state: Boolean) {
        viewModelScope.launch {
            dataStoreInteraction.setFirstLogin(state)
        }
    }
   fun checkVerification() {

        profileInteractionState.value = ProfileInteractionState.Interact(
            action = fun ()
            {
            verificationCheckButtonEnabled.value = false
            }
        )

       getFireAuth().currentUser!!.reload().addOnCompleteListener { task->
            profileInteractionState.value = ProfileInteractionState.OnComplete
            if (task.isSuccessful) {
                if(getFireAuth().currentUser?.isEmailVerified == true) {

                    profileInteractionState.value = ProfileInteractionState.IsSuccessful(
                        message = UiText.StringResource(R.string.auth_verifiednow,"asd").asString(applicationContext),
                        action = fun()
                        {
                            profileDataState.value = profileDataState.value.copy(profileVerified = true)
                            profileInteractionState.value = ProfileInteractionState.Idle
                        }
                    )



                }
                else {
                    profileInteractionState.value = ProfileInteractionState.Error(
                        message = UiText.StringResource(R.string.auth_verify_failed,"asd").asString(applicationContext),
                        action = fun()
                        {
                            verificationMailButtonEnabled.value = true
                            verificationCheckButtonEnabled.value = true
                            verificationMailButtonVisible.value = true
                            profileInteractionState.value = ProfileInteractionState.Idle
                        }
                    )



                }
            } else {
                profileInteractionState.value = ProfileInteractionState.Error(
                    message = UiText.StringResource(R.string.auth_fberror_internal,"asd").asString(applicationContext),
                    action = fun()
                    {
                        verificationMailButtonEnabled.value = true
                        verificationCheckButtonEnabled.value =true
                        profileInteractionState.value = ProfileInteractionState.Idle
                    }
                )


            }
        }

    }
    fun resendActivationMail() {
        profileInteractionState.value = ProfileInteractionState.Interact(
            action = fun ()
            {
                verificationMailButtonEnabled.value = false
            }
        )

        getFireAuth().currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
            profileInteractionState.value = ProfileInteractionState.OnComplete
            if (task.isSuccessful) {
                profileInteractionState.value = ProfileInteractionState.IsSuccessful(
                    message = UiText.StringResource(R.string.auth_resend_ver_mail,"asd").asString(applicationContext),
                    action = fun()
                    {
                        verificationMailButtonVisible.value = false
                        profileInteractionState.value = ProfileInteractionState.Idle
                    }
                )

            } else {
                profileInteractionState.value = ProfileInteractionState.Error(
                    message = UiText.StringResource(R.string.auth_fberror_internal,"asd").asString(applicationContext),
                    action = fun()
                    {

                        verificationMailButtonEnabled.value = true
                        profileInteractionState.value = ProfileInteractionState.Idle
                    }
                )



            }
        }

    }

}