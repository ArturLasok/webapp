package com.arturlasok.webapp.feature_auth.presentation.auth_onemessage

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.data.datasource.api.model.WebMessage
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.domain.model.Message
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.webapp.feature_auth.data.repository.ApiInteraction
import com.arturlasok.webapp.feature_auth.data.repository.RoomInteraction
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OneMessagesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val dataStoreInteraction: DataStoreInteraction,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction
    ):ViewModel() {

    val oneMessage = mutableStateOf(Message())
    val messageList = mutableStateOf<List<Message>>(listOf())
    val applicationContext = application


    init {

        Log.i(TAG, "init one message")



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

    //fun getAllMessagesFromRoom() : Flow<List<MessageEntity>> = roomInteraction.getAllMessagesFromRoom()

    fun getAllMessagesFromRoom() {
        roomInteraction.getAllMessagesFromRoom().onEach {
            Log.i(TAG, "Room message size: ${it.size}")
            messageList.value = it
        }.launchIn(viewModelScope)
    }
    fun getOneMessageFromRoom(messageId:String) {
        roomInteraction.getOneMessagesFromRoom(messageId).onEach { one->
            oneMessage.value = one
        }.launchIn(viewModelScope)
    }



}