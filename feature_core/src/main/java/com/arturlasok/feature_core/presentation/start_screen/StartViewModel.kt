package com.arturlasok.feature_core.presentation.start_screen

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.data.datasource.api.model.WebMessage
import com.arturlasok.feature_core.data.datasource.api.model.WebProject
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.domain.model.StartProjectsInteractionState
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val dataStoreInteraction: DataStoreInteraction,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction,

    ) : ViewModel() {


    val applications = mutableStateOf(application)

    var allProjects = Pair(mutableStateOf(listOf<WebProject>()), mutableStateOf<StartProjectsInteractionState>(StartProjectsInteractionState.Idle))

    init {

        getAllMessagesFromKtor()
        getAllProjectsFromKtor()

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
    fun getAllMessagesFromKtor() {

        roomInteraction.getAllMessagesIdsFromRoom().onEach { idList->

            apiInteraction.ktor_getAllMessagesFromUser(getFireAuth().currentUser?.uid ?: "unknown",getUserMail(),idList).onEach { apiMessageList->
                val readyList : MutableList<WebMessage> = mutableListOf()
                apiMessageList.onEach {
                    readyList.add(it.copy(_id = it._id.toString().substringAfter("oid=").substringBefore("}"),
                        //add sync -1 if is new message for this user
                        wMessage_sync = if(it.wMessage_viewedbyuser>0) 0 else -1))
                }
                //to rooom
                roomInteraction.insertAllMessageToRoom(apiInteraction.messageListFromApiToDomain(readyList)).onEach {
                    Log.i(TAG, "Room insert all messages response $it")

                }.launchIn(viewModelScope).join()

            }.launchIn(viewModelScope)

        }.launchIn(viewModelScope)

    }
    fun getProjectTemporaryTokenStore() : Flow<String> {
        return dataStoreInteraction.getProjectTemporaryToken()
    }
    fun getOpenProjectIdFromDataStore() : Flow<String> {
        return dataStoreInteraction.getOpenProjectId()
    }
    fun setOpenProjectIdInDataStore(projectId:String, action:()->Unit) {
        viewModelScope.launch {
            Log.i(TAG,"set project id: ${projectId.substringAfter("oid=").substringBefore("}")}")
            dataStoreInteraction.setOpenProjectId(projectId.substringAfter("oid=").substringBefore("}"))

        }.invokeOnCompletion {  action() }
    }
    fun getAllProjectsFromKtor() {
        allProjects.second.value = StartProjectsInteractionState.Interact
        getProjectTemporaryTokenStore().take(1).onEach { tempToken ->


            //check user or temp user
            val mail = if(getFireAuth().currentUser!=null) {
                getFireAuth().currentUser?.email } else { tempToken }

            val key = if(getFireAuth().currentUser!=null) {
                getFireAuth().currentUser?.uid } else { tempToken }


            apiInteraction.ktor_getUserProjects(key ?: "",mail ?: "").onEach { projectsList->

                allProjects.first.value = projectsList
                if(allProjects.first.value.isEmpty()) {

                    allProjects.second.value = StartProjectsInteractionState.Empty

                }
                else {
                    if (projectsList[0].wProject_sync == -1L) {
                        allProjects.second.value = StartProjectsInteractionState.Error(message = UiText.StringResource(
                            R.string.core_somethingWrong,
                            "asd"
                        ).asString(application.applicationContext))
                    } else{
                        allProjects.second.value = StartProjectsInteractionState.OnComplete
                    }


                }

            }.launchIn(viewModelScope)

        }.launchIn(viewModelScope)

    }
}