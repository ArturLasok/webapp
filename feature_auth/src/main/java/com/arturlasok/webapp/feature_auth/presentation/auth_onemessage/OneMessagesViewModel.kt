package com.arturlasok.webapp.feature_auth.presentation.auth_onemessage

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.domain.model.Message
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.webapp.feature_auth.model.MessageListGlobalState
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
    private val roomInteraction: RoomInteraction,
    private val messagesListGlobalState: MessageListGlobalState

    ):ViewModel() {

    val oneMessage = mutableStateOf(Message())
    val applicationContext = application

    init {
        Log.i(TAG, "init one message")
    }
    fun getMessagesGlobalState() : MessageListGlobalState {
        return messagesListGlobalState
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
    fun getOneMessageFromRoom() {
        messagesListGlobalState.getSelectedMessage().onEach {

            roomInteraction.getOneMessagesFromRoom(it).onEach { one->

                if(one.dMessage_sync<0L) {
                    updateSyncBecauseIsViewedByUser(one._did.toString())
                }
                oneMessage.value = one
            }.launchIn(viewModelScope)

        }.launchIn(viewModelScope)

    }
    fun updateSyncBecauseIsViewedByUser(messageId: String) {
        roomInteraction.updateOneMessageSetSync(messageId,System.currentTimeMillis()).onEach {

        }.launchIn(viewModelScope)

    }



}