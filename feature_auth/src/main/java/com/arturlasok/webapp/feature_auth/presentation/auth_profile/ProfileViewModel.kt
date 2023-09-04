package com.arturlasok.webapp.feature_auth.presentation.auth_profile

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
   //TODO SEND VER email on registration

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
            profileVerificationInteractionState = mutableStateOf<ProfileInteractionState>(ProfileInteractionState.Idle),
            profileInfoInteractionState = mutableStateOf<ProfileInteractionState>(ProfileInteractionState.Idle)
        )
    )

   // val profileVerificationInteractionState =
   // val profileInfoInteractionState =

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
        //get profile info
        getProfileInfoFormKtor()

    }

    fun getServerTime() {

            apiInteraction.getServerTime().onEach {

                serverTime.value = it

            }.launchIn(viewModelScope)

    }
    fun darkFromStore() : Flow<Int> {
        return dataStoreInteraction.getDarkThemeInt()
    }
    private fun getUserMail() : String {
        return getFireAuth().currentUser?.email ?: "empty"
    }
    fun getIsVerified() : Boolean {
        val ver = getFireAuth().currentUser?.isEmailVerified ?: false
        return ver
    }
    fun updateUserVerificationInKtor(key:String,mail:String) {
        apiInteraction.ktor_updateUserVerificationToTrue(key,mail).onEach {

        }.launchIn(viewModelScope)
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
    fun removeAllMessagesFromRoom() {
        roomInteraction.deleteAllMessagesFromRoom().onEach {
            Log.i(TAG, "Room all message delete $it")
        }.launchIn(viewModelScope)
    }
    private fun getProfileInfoFormKtor() {

        apiInteraction.ktor_getUserData(
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getFireAuth().currentUser?.email ?: "").onEach {
            if(it.webUserMail.isNotEmpty()) {
                profileDataState.value= profileDataState.value.copy(
                    profileCountry = it.webSimCountry,
                    profileLang = it.webUserLang,
                    profileMail = it.webUserMail)
                profileDataState.value = profileDataState.value.copy(profileInfoInteractionState = mutableStateOf(ProfileInteractionState.OnComplete))
            } else {
                profileDataState.value = profileDataState.value.copy(profileInfoInteractionState = mutableStateOf(ProfileInteractionState.Error(
                    message = UiText.StringResource(R.string.auth_somethingWrong,"asd").asString(applicationContext),
                    action = fun() {
                        profileDataState.value = profileDataState.value.copy(profileInfoInteractionState = mutableStateOf(ProfileInteractionState.Idle))
                    }
                )))

            }
        }.launchIn(viewModelScope)

    }
   fun checkVerification() {
       // val profileVerificationInteractionState = profileDataState.value.profileVerificationInteractionState
       profileDataState.value.profileVerificationInteractionState.value = ProfileInteractionState.Interact(
            action = fun ()
            {
            verificationCheckButtonEnabled.value = false
            }
        )

       getFireAuth().currentUser!!.reload().addOnCompleteListener { task->
           profileDataState.value.profileVerificationInteractionState.value  = ProfileInteractionState.OnComplete
            if (task.isSuccessful) {
                if(getFireAuth().currentUser?.isEmailVerified == true) {

                    profileDataState.value.profileVerificationInteractionState.value = ProfileInteractionState.IsSuccessful(
                        message = UiText.StringResource(R.string.auth_verifiednow,"asd").asString(applicationContext),
                        action = fun()
                        {
                            //update user verification in ktor
                            getFireAuth().currentUser?.email?.let {
                                getFireAuth().currentUser?.let { it1 ->
                                    updateUserVerificationInKtor(
                                        it1.uid,
                                        it
                                    )
                                }
                            }
                            profileDataState.value = profileDataState.value.copy(profileVerified = true)
                            profileDataState.value.profileVerificationInteractionState.value = ProfileInteractionState.Idle
                        }
                    )



                }
                else {
                    profileDataState.value.profileVerificationInteractionState.value  = ProfileInteractionState.Error(
                        message = UiText.StringResource(R.string.auth_verify_failed,"asd").asString(applicationContext),
                        action = fun()
                        {
                            verificationMailButtonEnabled.value = true
                            verificationCheckButtonEnabled.value = true
                            verificationMailButtonVisible.value = true
                            profileDataState.value.profileVerificationInteractionState.value  = ProfileInteractionState.Idle
                        }
                    )



                }
            } else {
                profileDataState.value.profileVerificationInteractionState.value  = ProfileInteractionState.Error(
                    message = UiText.StringResource(R.string.auth_fberror_internal,"asd").asString(applicationContext),
                    action = fun()
                    {
                        verificationMailButtonEnabled.value = true
                        verificationCheckButtonEnabled.value =true
                        profileDataState.value.profileVerificationInteractionState.value  = ProfileInteractionState.Idle
                    }
                )


            }
        }

    }
    fun resendActivationMail() {

        profileDataState.value.profileVerificationInteractionState.value  = ProfileInteractionState.Interact(
            action = fun ()
            {
                verificationMailButtonEnabled.value = false
            }
        )

        getFireAuth().currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
            profileDataState.value.profileVerificationInteractionState.value  = ProfileInteractionState.OnComplete
            if (task.isSuccessful) {
                profileDataState.value.profileVerificationInteractionState.value  = ProfileInteractionState.IsSuccessful(
                    message = UiText.StringResource(R.string.auth_resend_ver_mail,"asd").asString(applicationContext),
                    action = fun()
                    {
                        verificationMailButtonVisible.value = false
                        profileDataState.value.profileVerificationInteractionState.value  = ProfileInteractionState.Idle
                    }
                )

            } else {
                profileDataState.value.profileVerificationInteractionState.value  = ProfileInteractionState.Error(
                    message = UiText.StringResource(R.string.auth_fberror_internal,"asd").asString(applicationContext),
                    action = fun()
                    {

                        verificationMailButtonEnabled.value = true
                        profileDataState.value.profileVerificationInteractionState.value  = ProfileInteractionState.Idle
                    }
                )



            }
        }

    }

}