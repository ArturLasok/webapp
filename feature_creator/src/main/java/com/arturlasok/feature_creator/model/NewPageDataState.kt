package com.arturlasok.feature_creator.model

import androidx.compose.runtime.MutableState
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class NewPageDataState(

    val newPageName:String = "",
    val newPageProjectId: String ="",
    val newPageInsertState: MutableState<@Contextual ProjectInteractionState>

)
