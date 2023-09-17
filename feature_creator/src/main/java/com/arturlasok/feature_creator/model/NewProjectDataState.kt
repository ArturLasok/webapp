package com.arturlasok.feature_creator.model

import androidx.compose.runtime.MutableState
import com.arturlasok.feature_core.data.datasource.api.model.WebDomains
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class NewProjectDataState(

    val newProjectName: String = "",
    val newProjectAddress: String = "",
    val newProjectDomain: String = "",
    val newProjectAvailableDomainList: List<WebDomains> = listOf(),
    val newProjectReady : Boolean = false,
    val newProjectInteractionDomainsLoadState: MutableState<@Contextual ProjectInteractionState>,
    val newProjectInsertState: MutableState<@Contextual ProjectInteractionState>

    )
