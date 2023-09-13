package com.arturlasok.webapp.feature_auth.presentation.auth_messages

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
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
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.webapp.feature_auth.model.MessageListGlobalState
import com.arturlasok.webapp.feature_auth.model.ProfileInteractionState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    val dataStoreInteraction: DataStoreInteraction,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction,
    private val messagesListGlobalState: MessageListGlobalState
    ):ViewModel() {

    val deleteOneState = mutableStateOf<ProfileInteractionState>(ProfileInteractionState.Idle)
    val getKtorMessagesState = mutableStateOf<ProfileInteractionState>(ProfileInteractionState.Idle)
    val messageList = savedStateHandle.getStateFlow("messages", mutableListOf<Message>())
    val deletedItems =  mutableStateListOf<String>()
    val applicationContext = application
    val messageToDeleteId = mutableStateOf("")
    val messageSentView = mutableStateOf(false)

    init {
        Log.i(TAG, "init messages")
    }
    fun getMessagesGlobalState() : MessageListGlobalState {
        return messagesListGlobalState
    }
    fun setSelectedMessage(messageId:String, tab: Int) {
        messagesListGlobalState.currentBoxTabPosition.value = tab
        messagesListGlobalState.setSelectedMessage(messageId)
    }
    fun setSelectedTab(tab:Int) {
        messagesListGlobalState.currentBoxTabPosition.value = tab
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

    fun checkIsOneOpenIsDeleted(doAction:() -> Unit, oneMessage: MutableState<Message>) {
        if(oneMessage.value._did.toString()==messageToDeleteId.value) {
            doAction()
        }
    }
    fun getAllMessagesFromRoom() {

        roomInteraction.getAllMessagesFromRoom().onEach {
            Log.i(TAG, "Room get message, now size: ${it.size}")
            savedStateHandle["messages"] = it.toMutableList()
            getKtorMessagesState.value = ProfileInteractionState.OnComplete
        }.launchIn(viewModelScope)
    }
    fun deleteOneMessage(messageId: String) {


       if(getFireAuth().currentUser!=null) { getFireAuth().currentUser?.let { user ->
            apiInteraction.ktor_deleteOneMessage(user.uid, user.email ?: "", messageId).onEach { result ->

                Log.i(TAG, "delete result: $result")
                if (result) {
                    deletedItems.add(messageId)
                    delay(1000)
                    getMessagesGlobalState().itemToDeleteSelection.value = ""
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
                            getMessagesGlobalState().itemToDeleteSelection.value = ""
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
                   getMessagesGlobalState().itemToDeleteSelection.value = ""
                   messageToDeleteId.value = ""
                   deleteOneState.value  = ProfileInteractionState.Idle
               }
           )
       }


    }



}