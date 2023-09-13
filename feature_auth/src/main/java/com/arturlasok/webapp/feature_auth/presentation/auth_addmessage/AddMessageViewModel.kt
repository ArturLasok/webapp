package com.arturlasok.webapp.feature_auth.presentation.auth_addmessage

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.data.datasource.api.model.WebMessage
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.webapp.feature_auth.model.NewMessageDataState
import com.arturlasok.webapp.feature_auth.model.ProfileInteractionState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddMessageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val dataStoreInteraction: DataStoreInteraction,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction
    ):ViewModel() {

    private val newMessage= savedStateHandle.getStateFlow("newMessage","")
    private val newMessageTopic = savedStateHandle.getStateFlow("newMessageTopic","")
    private val newMessageContext = savedStateHandle.getStateFlow("newMessageContext","")
    private val newMessageTo = savedStateHandle.getStateFlow("newMessageTo","artpl81@gmail.com")

    val applicationContext = application

    val newMessageDataState = mutableStateOf(
        NewMessageDataState(
            newMessage = newMessage.value,
            newMessageTopic = newMessageTopic.value,
            newMessageContext = newMessageContext.value,
            newMessageTo = newMessageTo.value,
            newMessageSendInteractionState = mutableStateOf<ProfileInteractionState>(ProfileInteractionState.Idle)
        )
    )

    init {

        Log.i(TAG, "init new message")

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
    fun setNewMessageText(message: String) {
        savedStateHandle["newMessage"] = message
        newMessageDataState.value = newMessageDataState.value.copy(newMessage =message)
    }
    fun setNewMessageTopic(messageTopic: String) {
        savedStateHandle["newMessageTopic"] = messageTopic
        newMessageDataState.value = newMessageDataState.value.copy(newMessageTopic = messageTopic)
    }
    fun setNewMessageContext(messageContext: String) {
        savedStateHandle["newMessageContext"] = messageContext
        newMessageDataState.value = newMessageDataState.value.copy(newMessageContext = messageContext)
    }
    fun setNewMessageTo(messageTo:String) {
        savedStateHandle["newMessageTo"] = messageTo
        newMessageDataState.value = newMessageDataState.value.copy(newMessageTo = messageTo)
    }
    fun getOneMessageFromRoom(messageId:String) {

        roomInteraction.getOneMessagesFromRoom(messageId).onEach { one->

            setNewMessageTo(one.dMessage_author_mail)

            setNewMessageTopic((if(one.dMessage_title.contains("Re: ")) { "" } else "Re: ")+one.dMessage_title)

            setNewMessageContext(one._did.toString())
           // setNewMessageText(one.dMessage_content)
            newMessageDataState.value = newMessageDataState.value.copy(
                newMessageTo = newMessageTo.value,
                newMessageContext = newMessageContext.value,
                newMessageTopic = newMessageTopic.value,
                newMessage = newMessage.value,
                newMessageSendInteractionState = mutableStateOf(ProfileInteractionState.Idle)
            )
        }.launchIn(viewModelScope)
    }
    private fun sendMessageToKtor(action:(ret: Boolean)->Unit) {
        //action(true)

        try {
            getFireAuth().currentUser?.let { fuser ->

                val lang = "unknown"
                val country = "unknown"

                    apiInteraction.ktor_insertMessage(
                        fuser.uid,
                        WebMessage(
                            _id = null,
                            wMessage_id = 0L,
                            wMessage_title = newMessageDataState.value.newMessageTopic.replaceFirstChar { it.uppercase() },
                            wMessage_content = newMessageDataState.value.newMessage,
                            wMessage_author_mail = fuser.email ?: "unknown",
                            wMessage_context = newMessageDataState.value.newMessageContext,
                            wMessage_added = System.currentTimeMillis(),
                            wMessage_edited = 0L,
                            wMessage_user_mail = newMessageDataState.value.newMessageTo,
                            wMessage_user_lang = lang,
                            wMessage_user_country = country,
                            wMessage_viewedbyuser = 0L,
                            wMessage_sync = 0L,
                        )
                    ).onEach { response ->
                        if(response) action(true) else action(false)
                    }.launchIn(viewModelScope)




            }
        }
        catch (e:Exception) { action(false) }



    }
    fun sendMessage() {

        newMessageDataState.value.newMessageSendInteractionState.value = ProfileInteractionState.Interact(
            action = fun () {
                //no action at this point
            }
        )
        //check form
        if(newMessageDataState.value.newMessageTopic.isNotEmpty() && newMessageDataState.value.newMessage.isNotEmpty() && getFireAuth().currentUser?.isEmailVerified == true) {

            //ok to send
            sendMessageToKtor(
                action = {result->
                if(result) {

                    newMessageDataState.value.newMessageSendInteractionState.value =
                        ProfileInteractionState.IsSuccessful(
                            message = UiText.StringResource(R.string.auth_messageSent, "asd")
                                .asString(applicationContext),
                            action = fun() {
                               // newMessageDataState.value.newMessageSendInteractionState.value = ProfileInteractionState.OnComplete
                                newMessageDataState.value = newMessageDataState.value.copy(newMessage="",newMessageTopic="",newMessageContext="",newMessageTo="")
                                newMessageDataState.value.newMessageSendInteractionState.value =
                                    ProfileInteractionState.OnComplete
                            }
                        )
                } else {
                    newMessageDataState.value.newMessageSendInteractionState.value  = ProfileInteractionState.Error(
                        message = UiText.StringResource(R.string.auth_fberror_internal,"asd").asString(applicationContext),
                        action = fun()
                        {
                            newMessageDataState.value.newMessageSendInteractionState.value  = ProfileInteractionState.Idle
                        }
                    )
                }
            })






        }
        else {
            //form errors
            if(newMessageDataState.value.newMessageTopic.isEmpty()) {
                newMessageDataState.value.newMessageSendInteractionState.value  = ProfileInteractionState.Error(
                    message = UiText.StringResource(R.string.auth_notopic,"asd").asString(applicationContext),
                    action = fun()
                    {
                        newMessageDataState.value.newMessageSendInteractionState.value  = ProfileInteractionState.Idle
                    }
                )
            }
            else if (newMessageDataState.value.newMessage.isEmpty()){
                newMessageDataState.value.newMessageSendInteractionState.value  = ProfileInteractionState.Error(
                    message = UiText.StringResource(R.string.auth_nomessageContent,"asd").asString(applicationContext),
                    action = fun()
                    {
                        newMessageDataState.value.newMessageSendInteractionState.value  = ProfileInteractionState.Idle
                    }
                )
            } else
            {
                newMessageDataState.value.newMessageSendInteractionState.value  = ProfileInteractionState.Error(
                    message = UiText.StringResource(R.string.auth_verify_failed,"asd").asString(applicationContext),
                    action = fun()
                    {
                        newMessageDataState.value.newMessageSendInteractionState.value  = ProfileInteractionState.Idle
                    }
                )
            }

        }


    }

}