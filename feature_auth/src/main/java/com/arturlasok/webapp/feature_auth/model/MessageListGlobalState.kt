package com.arturlasok.webapp.feature_auth.model

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageListGlobalState @Inject constructor(
    private val dataStoreInteraction: DataStoreInteraction
):ViewModel() {

    val itemToDeleteSelection = mutableStateOf("")
    val currentBoxTabPosition = mutableStateOf(0)
    fun getSelectedMessage() : Flow<String> {
        Log.i(TAG, "MESSSAGES GET ${dataStoreInteraction.getSelectedMessage()}")
        return dataStoreInteraction.getSelectedMessage()
    }
    fun setSelectedMessage(messageId: String) {
        Log.i(TAG, "MESSSAGES SET ${messageId}")
        viewModelScope.launch {
            dataStoreInteraction.setSelectedMessage(messageId)
        }
    }



}