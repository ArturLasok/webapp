package com.arturlasok.webapp.feature_auth.model

import androidx.compose.runtime.MutableState

data class NewMessageDataState(
    val newMessage : String = "",
    val newMessageTopic: String ="",
    val newMessageContext : String = "",
    val newMessageTo: String ="",
    val newMessageSendInteractionState: MutableState<ProfileInteractionState>,
)
