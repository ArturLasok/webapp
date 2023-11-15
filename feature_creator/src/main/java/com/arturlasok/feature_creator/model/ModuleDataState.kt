package com.arturlasok.feature_creator.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ModuleDataState(
    val projectId: MutableState<String>,

    val projectGetPagesState: MutableState<@Contextual ProjectInteractionState>,
    val projectSelectedPageId: MutableState<String>,
    val projectPageName: MutableState<String>,

    @Contextual
    val projectModulesList: SnapshotStateList<WebLayout>,


    )
