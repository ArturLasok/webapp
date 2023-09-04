package com.arturlasok.webapp.feature_auth.presentation.auth_messages

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.data.datasource.api.model.WebMessage
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.domain.model.Message
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.webapp.feature_auth.data.repository.ApiInteraction
import com.arturlasok.webapp.feature_auth.data.repository.RoomInteraction
import com.arturlasok.webapp.feature_auth.model.ProfileInteractionState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val dataStoreInteraction: DataStoreInteraction,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction
    ):ViewModel() {

    val deleteOneState = mutableStateOf<ProfileInteractionState>(ProfileInteractionState.Idle)
    val getKtorMessagesState = mutableStateOf<ProfileInteractionState>(ProfileInteractionState.Idle)
    val messageList = mutableStateOf<List<Message>>(listOf())
    val applicationContext = application
    val messageToDeleteId = mutableStateOf("")
    private val currentMailTabPosition = mutableStateOf(0)


    init {

        Log.i(TAG, "init newses")
        //getAllMessagesFromKtor()


    }

    fun getServerTime() {

            apiInteraction.getServerTime().onEach {



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
    fun haveNetwork() : Boolean {
        return isOnline.isNetworkAvailable.value
    }
    fun getFireAuth() : FirebaseAuth {
        return fireAuth
    }
    private fun getMailFollowFromDataStore() : Flow<String> {
        return dataStoreInteraction.getMailFollow()
    }
    //zwraca ADD Screen tab Asortymen position
    fun getCurrentMailTabPos() : MutableState<Int> {
        return currentMailTabPosition
    }
    //ustawia ADD Screen tab Asortyment position
    fun setCurrentMailTabPos(index : Int) {
        currentMailTabPosition.value = index
    }
    fun getAllMessagesFromKtor() {
        getKtorMessagesState.value = ProfileInteractionState.Interact(action = {})
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
                getAllMessagesFromRoom()

            }.launchIn(viewModelScope)

        }.launchIn(viewModelScope)

    }
    //fun getAllMessagesFromRoom() : Flow<List<MessageEntity>> = roomInteraction.getAllMessagesFromRoom()

    fun getAllMessagesFromRoom() {

        roomInteraction.getAllMessagesFromRoom().onEach {
            Log.i(TAG, "Room get message, now size: ${it.size}")
            messageList.value = it
            getKtorMessagesState.value = ProfileInteractionState.OnComplete
        }.launchIn(viewModelScope)
    }
    fun removeAllMessagesFromRoom() {
        roomInteraction.deleteAllMessagesFromRoom().onEach {
            Log.i(TAG, "Room all message delete $it")
        }.launchIn(viewModelScope)
    }
    fun deleteOneMessage(messageId: String) {


       if(getFireAuth().currentUser!=null) { getFireAuth().currentUser?.let { user ->
            apiInteraction.ktor_deleteOneMessage(user.uid, user.email ?: "", messageId).onEach { result ->
                Log.i(TAG, "delete result: $result")
                if (result) {
                    roomInteraction.deleteMessageFromRoom(messageId).launchIn(viewModelScope)
                    deleteOneState.value = ProfileInteractionState.IsSuccessful(
                        message = UiText.StringResource(R.string.auth_messageDelete, "asd")
                            .asString(applicationContext),
                        action = fun() {
                            getAllMessagesFromRoom()
                            messageToDeleteId.value = ""
                            deleteOneState.value = ProfileInteractionState.Idle
                        })
                } else {
                    deleteOneState.value = ProfileInteractionState.Error(
                        message = UiText.StringResource(R.string.auth_fberror_internal, "asd")
                            .asString(applicationContext),
                        action = fun() {
                            messageToDeleteId.value = ""
                            deleteOneState.value = ProfileInteractionState.Idle
                        }
                    )
                }
            }.launchIn(viewModelScope)
            }
        }
       else
       {
           deleteOneState.value = ProfileInteractionState.Error(
               message = UiText.StringResource(R.string.auth_fberror_internal,"asd").asString(applicationContext),
               action = fun()
               {
                   messageToDeleteId.value = ""
                   deleteOneState.value  = ProfileInteractionState.Idle
               }
           )
       }


    }



}