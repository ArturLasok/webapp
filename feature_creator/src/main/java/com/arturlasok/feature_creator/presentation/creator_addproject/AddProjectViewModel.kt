package com.arturlasok.feature_creator.presentation.creator_addproject

import android.app.Application
import android.content.ClipData
import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ClipboardManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.data.datasource.api.model.WebDomains
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.NewProjectDataState
import com.arturlasok.feature_creator.model.ProjectInteractionState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AddProjectViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val dataStoreInteraction: DataStoreInteraction,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction,

    ) : ViewModel() {

    private val newProjectName = savedStateHandle.getStateFlow("newProjectName","")
    private val newProjectAddress = savedStateHandle.getStateFlow("newProjectAddress","")
    private val newProjectDomain = savedStateHandle.getStateFlow("newProjectDomain","")
    private val newProjectReady = savedStateHandle.getStateFlow("newProjectReady",false)
    val isDomainRed = mutableStateOf(false)

    val applicationContext = application

    val newProjectDataState = mutableStateOf(
        NewProjectDataState(
            newProjectName = newProjectName.value,
            newProjectAddress = newProjectAddress.value,
            newProjectDomain = newProjectDomain.value,
            newProjectReady = newProjectReady.value,
            newProjectAvailableDomainList = listOf(),
            newProjectInteractionDomainsLoadState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle),
            newProjectInsertState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle)

        )
    )

    init {
        getDomainsFromKtor()
    }
    fun isSelectedAddressIsValid(address: String) : Boolean {
        val ADDRESS_PATTERN = Pattern.compile(
            "[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?"
        )
        return ADDRESS_PATTERN.matcher(address).matches()
    }
    fun isSelectedDomainIsRed() : Boolean {
        val domain = newProjectDataState.value.newProjectAvailableDomainList.find {
            it.wDomain_address == newProjectDataState.value.newProjectDomain
        }
        if (domain != null) {
            isDomainRed.value = ((!domain.wDomain_enabled))

        } else { isDomainRed.value = false}

        return if (domain != null) {
            !domain.wDomain_enabled
        } else {
            false
        }
    }
    fun setNewProjectName(name:String) {
        savedStateHandle["newProjectName"] = name
        newProjectDataState.value = newProjectDataState.value.copy(newProjectName = name)
    }
    fun setNewProjectAddress(address: String) {
        savedStateHandle["newProjectAddress"] = address
        newProjectDataState.value = newProjectDataState.value.copy(newProjectAddress = address)
    }
    fun setNewProjectReady(ready: Boolean) {
        savedStateHandle["newProjectReady"] = ready
        newProjectDataState.value = newProjectDataState.value.copy(newProjectReady = ready)
    }
    fun setNewProjectDomain(domainId: String) {
        savedStateHandle["newProjectDomain"] = domainId
        newProjectDataState.value = newProjectDataState.value.copy(newProjectDomain = domainId)
    }
   fun setNewProjectInteractionDomainsLoadState(newState: ProjectInteractionState) {
        newProjectDataState.value = newProjectDataState.value.copy(newProjectInteractionDomainsLoadState = mutableStateOf(newState))
    }
    private fun setNewProjectAvailableDomainList(newList: List<WebDomains>) {
        newProjectDataState.value = newProjectDataState.value.copy(newProjectAvailableDomainList = newList)
    }
    fun setNewProjectInsertState(newState: ProjectInteractionState) {
        newProjectDataState.value = newProjectDataState.value.copy(newProjectInsertState = mutableStateOf(newState))
    }
    fun setProjectTemporaryToken(token: String) {
        viewModelScope.launch{
            dataStoreInteraction.setProjectTemporaryToken(token)
        }
    }
    fun getProjectTemporaryTokenStore() : Flow<String> {
        return dataStoreInteraction.getProjectTemporaryToken()
    }
    fun insertNewProject() {
        //
        if(newProjectDataState.value.newProjectDomain.isNotEmpty() && newProjectDataState.value.newProjectAddress.isNotEmpty() && !isDomainRed.value && isSelectedAddressIsValid(newProjectDataState.value.newProjectAddress)) {
            getProjectTemporaryTokenStore().take(1).onEach { tempToken ->
                Log.i(TAG, "TOKEN IS EMPTY!!!!: ${tempToken}")
                var token = ""
                if (tempToken.isEmpty()) {
                    val unixTime = System.currentTimeMillis() / 1000L
                    val allowedChars = ('A'..'Z') + ('a'..'s') + ('0'..'9')
                    val genTokenForThisMobile: () -> String = fun(): String {
                        return (1..16)
                            .map { allowedChars.random() }
                            .joinToString("")
                    }
                    token = unixTime.toString() + "time" + genTokenForThisMobile.invoke()
                    setProjectTemporaryToken(token)
                } else {
                    token = tempToken
                }
                val sim =
                    application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val tmr = sim.networkCountryIso
                apiInteraction.ktor_insertProject(
                    projectName = newProjectAddress.value +"."+ newProjectDataState.value.newProjectDomain,
                    projectAddress = newProjectAddress.value,
                    projectDomain = newProjectDomain.value,
                    token = token,
                    key = fireAuth.currentUser?.uid ?: token,
                    mail = fireAuth.currentUser?.email ?: token,
                    simCountry = tmr.uppercase()
                ).onEach { response ->

                    if (response) {
                        setNewProjectInsertState(ProjectInteractionState.IsSuccessful(
                            message = UiText.StringResource(R.string.core_insert_create,"asd").asString(applicationContext)))
                        delay(1000)
                        setNewProjectInsertState(ProjectInteractionState.OnComplete)
                        setNewProjectReady(true)
                    } else {
                        setNewProjectInsertState(ProjectInteractionState.Error(
                            message = UiText.StringResource(R.string.core_insert_apierror,"asd").asString(applicationContext)))

                    }


                }.launchIn(viewModelScope)


            }.launchIn(viewModelScope)


        } else {
            //Form error
            if(isDomainRed.value) {
                setNewProjectInsertState(
                    ProjectInteractionState.Error(
                        message = UiText.StringResource(
                            R.string.core_insert_form_error_domain,
                            "asd"
                        ).asString(applicationContext)
                    )
                )
            } else {
                if(newProjectDataState.value.newProjectAddress.isEmpty() || !isSelectedAddressIsValid(newProjectDataState.value.newProjectAddress)) {
                    setNewProjectInsertState(
                        ProjectInteractionState.Error(
                            message = UiText.StringResource(
                                R.string.core_insert_form_error_address,
                                "asd"
                            ).asString(applicationContext)
                        )
                    )
                }
                else {
                    setNewProjectInsertState(
                        ProjectInteractionState.Error(
                            message = UiText.StringResource(
                                R.string.core_insert_form_error_domain,
                                "asd"
                            ).asString(applicationContext)
                        )
                    )
                }
            }

        }
    }
    fun getDomainsFromKtor() {

        setNewProjectInteractionDomainsLoadState(ProjectInteractionState.Interact)

        apiInteraction.ktor_getDomainsData(newProjectDataState.value.newProjectAddress).dropWhile {
            (it.third!=newProjectDataState.value.newProjectAddress) && it.third!="FALSE!"
        }.onEach {domainsList->
            Log.i(TAG,"CHIP list ALL: ${domainsList.second.size}, list AVA: ${domainsList.first.size}")
            val readyList : MutableList<WebDomains> = mutableListOf()
            domainsList.second.onEach {OneOfAllDomanin->

                if(domainsList.first.any {
                        it.wDomain_address == OneOfAllDomanin.wDomain_address
                    }) {
                    Log.i(TAG,"CHIP add with true  ${OneOfAllDomanin.wDomain_address}")
                    readyList.add(OneOfAllDomanin.copy(_id = OneOfAllDomanin._id.toString().substringAfter("oid=").substringBefore("}"), wDomain_enabled = true))
                }
                else {
                    Log.i(TAG,"CHIP add with false  ${OneOfAllDomanin.wDomain_address}")
                    readyList.add(OneOfAllDomanin.copy(_id = OneOfAllDomanin._id.toString().substringAfter("oid=").substringBefore("}"), wDomain_enabled = false))
                }

            }

            setNewProjectAvailableDomainList(readyList)


            if(readyList.size==0) {
                setNewProjectInteractionDomainsLoadState(ProjectInteractionState.Error())
            }
            else {
                setNewProjectInteractionDomainsLoadState(ProjectInteractionState.IsSuccessful())
            }

        }.launchIn(viewModelScope)

    }
    fun darkFromStore() : Flow<Int> {
        return dataStoreInteraction.getDarkThemeInt()
    }
    fun haveNetwork() : Boolean {
        return isOnline.isNetworkAvailable.value
    }
    fun getFireAuth() : FirebaseAuth {
        return fireAuth
    }
    private fun getUserMail() : String {
        return getFireAuth().currentUser?.email ?: "empty"
    }
    fun numberNotViewedMessages() : Flow<Int> =roomInteraction.getCountNotViewedMessages(getUserMail())

}